# API

## Endpoint

POST /api/generate-report

## Popis

Endpoint pro generování reportu.

## Parametry

### Query parametry

| Parametr     | Typ    | Povinné | Default | Popis                                                                   |
|--------------|--------|---------|---------|-------------------------------------------------------------------------|
| **format**   | string | ne      | pdf     | Formát výsledného reportu. Podporováno: `pdf`, `html`, `typst`.         |
| **filename** | string | ne      | report  | Název výsledného souboru bez přípony. Přípona se doplní podle `format`. |

## Example of request Body

```json
{
  "startDate": "2021-01-01",
  "endDate": "2022-04-30",

  "tenant": [
    "Marie Černá",
    "Jindřišská 16",
    "111 50 Praha 1"
  ],

  "owner": [
    "Jan Novák",
    "majitel@example.com"
  ],

  "reportPlace": "V Praze",
  "reportDate": "2022-05-22",

  "sources": [
    "Vyúčtování služeb od Společenství vlastníků za rok 2021",
    "Příloha č. 3 k vyhlášce č.269/2015 Sb.",
    "Měsíční předpis záloh pro rok 2022 od Společenství vlastníků",
    "https://www.pvk.cz/vse-o-vode/cena-vodneho-a-stocneho/vyvoj-vodneho-a-stocneho-v-praze/"
  ],

  "deposits": [
    {
      "name": "deposits",
      "payments": [
        { "description": "jednorázově", "count": 1, "amount": 43500 }
      ]
    }
  ],

  "heating": [
    {
      "name": "heating",
      "heatingFees": [
        { "annualCost": 8712.9, "startDate": "2021-01-01", "endDate": "2021-12-31" },
        { "annualCost": 8472.0, "startDate": "2022-01-01", "endDate": "2022-12-31" }
      ]
    }
  ],

  "otherFees": [
    {
      "name": "other_fees",
      "otherFees": [
        { "annualCost": 8772, "startDate": "2021-01-01", "endDate": "2021-12-31" },
        { "annualCost": 8508, "startDate": "2022-01-01", "endDate": "2022-12-31" }
      ]
    }
  ],

  "coldWater": [
    {
      "name": "cold_water",
      "readings": [
        { "meterId": "1", "readingDate": "2021-01-01", "state": 158.1 },
        { "meterId": "1", "readingDate": "2021-10-15", "state": 184.4 },
        { "meterId": "2", "readingDate": "2021-10-15", "state": 0 },
        { "meterId": "2", "readingDate": "2022-01-01", "state": 8.6 },
        { "meterId": "2", "readingDate": "2022-05-01", "state": 21 }
      ],
      "priceList": [
        { "startDate": "2021-01-01", "endDate": "2021-12-31", "pricePerCubicMeter": 103.4076 },
        { "startDate": "2022-01-01", "endDate": "2022-12-31", "pricePerCubicMeter": 108.13 }
      ]
    }
  ],

  "hotWater": [
    {
      "name": "hot_water",
      "readings": [
        { "meterId": "1", "readingDate": "2021-01-01", "state": 96.6 },
        { "meterId": "1", "readingDate": "2021-10-15", "state": 106.5 },
        { "meterId": "2", "readingDate": "2021-10-15", "state": 0 },
        { "meterId": "2", "readingDate": "2022-01-01", "state": 5.5 },
        { "meterId": "2", "readingDate": "2022-05-01", "state": 13.3 }
      ],
      "priceList": [
        { "startDate": "2021-01-01", "endDate": "2021-12-31", "pricePerCubicMeter": 81.49536 },
        { "startDate": "2022-01-01", "endDate": "2022-12-31", "pricePerCubicMeter": 108.13 }
      ],
      "heatingBasicCosts": [
        { "annualCost": 5000.0, "startDate": "2021-01-01", "endDate": "2021-12-31" },
        { "annualCost": 5200.0, "startDate": "2022-01-01", "endDate": "2022-12-31" }
      ],
      "heatingConsumableTariffs": [
        { "startDate": "2021-01-01", "endDate": "2021-12-31", "pricePerCubicMeter": 120.0 },
        { "startDate": "2022-01-01", "endDate": "2022-12-31", "pricePerCubicMeter": 130.0 }
      ]
    }
  ],

  "custom": []
}
```

## Response

### 200 OK
Vrací binární obsah generovaného reportu.

**Headers:**
| Header               | Popis |
|----------------------|--------|
| Content-Type         | MIME typ dle zvoleného formátu (`application/pdf`, `text/html`, `text/plain` pro typst). |
| Content-Disposition  | `attachment; filename="{filename}.{format}"` |

**Body:**
Binární data výsledného souboru (PDF/HTML/TYPST).
