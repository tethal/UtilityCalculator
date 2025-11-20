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
  "start_date": "2024-02-15",
  "end_date": "2024-12-31",
  "tenant": [
    "Jméno nájemníka",
    "Adresa nemovitosti"
  ],
  "owner": [
    "Jméno majitele",
    "majitel@example.com"
  ],
  "report_place": "V Praze",
  "report_date": "2025-05-20",
  "sources": [
    "vyúčtování SVJ za rok 2024"
  ],
  "deposits": [
    {
      "description": "leden - duben",
      "count": 4,
      "amount": 3000
    },
    {
      "description": "květen",
      "amount": 3500
    },
    {
      "description": "červen - prosinec",
      "count": 7,
      "amount": 3500
    }
  ],
  "heating": [
    {
      "annual_cost": 10992,
      "start_date": "2021-01-01",
      "end_date": "2021-12-31"
    },
    {
      "annual_cost": 10992,
      "start_date": "2022-01-01",
      "end_date": "2022-12-31"
    }
  ],
  "other_fees": [
    {
      "start_date": "2021-01-01",
      "end_date": "2021-12-31",
      "annual_cost": 3000
    },
    {
      "start_date": "2022-01-01",
      "end_date": "2022-12-31",
      "annual_cost": 3200
    }
  ],
  "cold_water": {
    "reading": [
      {
        "meter_id": "1",
        "reading_date": "2021-10-14",
        "state": 184.4
      },
      {
        "meter_id": "2",
        "reading_date": "2021-12-31",
        "state": 8.6
      },
      {
        "meter_id": "2",
        "reading_date": "2022-04-30",
        "state": 21
      }
    ],
    "tariff": [
      {
        "start_date": "2021-01-01",
        "end_date": "2021-12-31",
        "unit_amount": 103.4076
      },
      {
        "start_date": "2022-01-01",
        "end_date": "2022-12-31",
        "unit_amount": 108.13
      }
    ]
  },
  "hot_water": {
    "reading": [
      {
        "meter_id": "1",
        "reading_date": "2021-10-14",
        "state": 100.5
      },
      {
        "meter_id": "2",
        "reading_date": "2021-12-31",
        "state": 55.0
      },
      {
        "meter_id": "2",
        "reading_date": "2022-04-30",
        "state": 80.75
      }
    ],
    "tariff": [
      {
        "start_date": "2021-01-01",
        "end_date": "2021-12-31",
        "unit_amount": 145.50
      },
      {
        "start_date": "2022-01-01",
        "end_date": "2022-12-31",
        "unit_amount": 150.75
      }
    ],
    "heating_basic_cost": [
      {
        "start_date": "2021-01-01",
        "end_date": "2021-12-31",
        "annual_cost": 5000.0
      },
      {
        "start_date": "2022-01-01",
        "end_date": "2022-12-31",
        "annual_cost": 5200.0
      }
    ],
    "heating_consumable_tariff": [
      {
        "start_date": "2021-01-01",
        "end_date": "2021-12-31",
        "unit_amount": 120.0
      },
      {
        "start_date": "2022-01-01",
        "end_date": "2022-12-31",
        "unit_amount": 130.0
      }
    ]
  }
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
