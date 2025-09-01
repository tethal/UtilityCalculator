# Obecne
- vstupni soubor se cte po radcich
- znak '#' oznacuje komentar, vse za nim se odstrani
- odstrani se mezery na zacatku a konci radku
- prazdne radky se ignoruji
- prvni dva radky jsou hlavicka:
  - prvni (neprazdny) radek obsahuje `RANGE` (viz nize)
  - druhy (neprazdny) radek obsahuje misto a datum oddelene carkou
- ostatni radky tvori skupiny, kazda skupina zacina radkem s hranatymi zavorkami
- tzn. treti radek musi byt zacatek skupiny `[...]`
- kazda skupina muze byt na vstupu nejvyse jednou
- na vstupu mohou byt pouze nize uvedene skupiny

```
2024
V Praze, 2025-05-14

[group1]
row1            # komentar
row2

[group2]
row1
row2
```

## Skupiny hlavicky
- `[najemnik]`, `[najemce]`, `[zdroje]`
- radky v techto skupinach se nijak neparsuji, rovnou se ulozi do odpovidajicich seznamu v reportu - `tenant`, `owner`, `sources`
```
[najemnik]
    Marie Černá
    Jindřišská 16
    111 50 Praha 1

[najemce]
    Jan Novák
    majitel@example.com

[zdroje]
    Vyúčtování služeb od Společenství vlastníků za rok 2024
```

## Skupiny pro oddily
- `[zalohy]`, `[vytapeni]`, `[ostatni poplatky]`, `[studena voda]`, `[tepla voda]`
- za nazvem skupiny muze byt dvojtecka a libovolny text, ktery se pouzije jako nazev oddilu (`SectionInputs.name`)
  - `[ostatni poplatky:Náklady]` vytvori `OtherFeeInputs` s nazvem `"Náklady"`
  - `[ostatni poplatky]` vytvori `OtherFeeInputs` s defaultnim nazvem `"Ostatní poplatky"` 

## `RANGE`
- reprezentuje instanci `DateRange` a muze mit nekolik podob:
- `YYYY-MM-DD...YYYY-MM-DD`
  - interval od-do **vcetne**, tj. ke koncovemu datumu je pri parsovani potreba pricist jeden den:
  - `2025-03-04...2025-08-12` reprezentuje range `[2025-03-04, 2025-08-13)`
- `YYYY-MM...YYYY-MM`
  - interval od zacatku jednoho mesice do konce druheho, opet je nutne pricist jednicku:
  - `2025-03...2025-12` je zkratka za `2025-03-01...2025-12-31` a reprezentuje range `[2025-03-01, 2026-01-01)`
- `YYYY...YYYY`
  - interval od zacatku jednoho roku do konce druheho, opet je nutne pricist jednicku:
  - `2022...2023` je zkratka za `2022-01-01...2023-31-12` a reprezentuje range `[2022-01-01, 2024-01-01)`
- `YYYY-MM`
  - cely mesic
  - `2025-03` je zkratka za `2025-03...2025-03` a reprezentuje range `[2025-03-01, 2025-04-01)`
- `YYYY`
  - cely rok
  - `2025` je zkratka za `2025...2025` a reprezentuje range `[2025-01-01, 2026-01-01)`

## `EXPR`
- libovolny aritmeticky vyraz s operatory `+`, `-`, `*`, `/` a zavorkami
- `1+2*3` se parsuje jako `1 + (2 * 3) = 7`
- `1*2+3` se parsuje jako `(1 * 2) + 3 = 5`
- `6-3-1` se parsuje jako `(6 - 3) - 1 = 2`
- `9+8-7*(6/5)` se parsuje jako `(9 + 8) - (7 * (6 / 5)) = 8.6`
- vypocet s presnosti na 10 desetinnych mist
- deleni nulou vede k `ArithmeticException` a nijak neresime 
- gramatika:
```
expr
    : expr + term 
    | expr - term 
    | term
term
    : term * factor 
    | term / factor 
    | factor
factor
    : NUMBER 
    | ( expr )
```
- `NUMBER` je `BigDecimal` ve formatu `[0-9]+(.[0-9]+)?`

# `[zalohy]`
- kazdy radek odpovida jedne instanci `Payment`
- na radku musi byt jedna dvojtecka
- vse pred dvojteckou je `description` a nijak se neparsuje
- za dvojteckou je bud `EXPR x EXPR` (`count` x `unitAmount`) nebo jen `EXPR` (count je v pak implicitne 1)
- priklady:
  - `leden: 4.4 + (5 / 2)`
    - description: "leden", count: 1, unitAmount: 6.9
  - `unor - prosinec: 12 - 1 x 666`
    - description: "unor - prosinec", count: 11, unitAmount: 666.0

# `[vytapeni]` a `[ostatni poplatky]`
- kazdy radek odpovida jedne instanci `ServiceCost`
- na radku musi byt jedna dvojtecka
- pred dvojteckou je `RANGE` (`dateRange`), za dvojteckou je `EXPR` (`annualCost`)
- priklad:
  - `2025-03...2025-04: 45+0.5`
    - dateRange: [2025-03-01, 2025-05-01), annualCost: 45.5`

# `[studena voda]`
- radek zacina bud `O` (odecet) nebo `SV` (cena studene vody)

## Odecet
- kazdy radek odpovida jedne nebo vice instanci `MeterReading`
- na radku je `O`, datum (YYYY-MM-DD), dvojtecka a carkami oddelene odecty
- kazdy odecet je id vodomeru, zavinac a `EXPR`
- pokud je na radku jen jeden odecet, tak muze byt id vodomeru vynachane a je implicitne "SV"
- priklady:
  - `O 2024-01-01: SV1 @ 14.5, SV2 @ 55.6`
  - `O 2024-01-01: 66.6`

## Cena studene vody
- kazdy radek odpovida jedne nebo vice instanci `WaterTariff`
- na radku je `SV`, `RANGE`, dvojtecka a `EXPR`
- priklady:
  - `SV 2024: 5920.16 / 42.2`

# `[tepla voda]`
- podobne jako studena (implicitni nazev vodomeru je "TV")
- krome `O` a `SV` ma jeste dva dalsi typy radku:
- `ZS 2024: 2725.92`
  - zakladni slozka, kazdy radek je jedna instance `ServiceCost` v `heatingBasicCosts`
  - na radku je `ZS`, `RANGE`, dvojtecka a `EXPR`
- `SS 2024: 257.988`
  - spotrebni slozka, kazdy radek je jedna instance `WaterTariff` v `heatingConsumableTariffs`
  - na radku je `SS`, `RANGE`, dvojtecka a `EXPR`
