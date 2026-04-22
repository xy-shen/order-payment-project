# Frontend

This Angular app is a minimal manual-testing UI for the backend APIs in this repository.

It covers:

- create order
- list all orders
- find order by id
- update order status
- list all payments
- find payment by id
- update payment status

## Backend Services

The frontend expects these services to be running locally:

- order-service on `http://localhost:8081`
- payment-service on `http://localhost:8082`

The Angular dev server uses [`proxy.conf.json`](/Users/shenxinyi/Projects/final-project/frontend/proxy.conf.json) so the frontend can call:

- `/order-api/...` -> `http://localhost:8081/...`
- `/payment-api/...` -> `http://localhost:8082/...`

## Run Locally

From the `frontend` directory:

```bash
npm install
npm start
```

Then open:

```text
http://localhost:4200
```

## Build

```bash
npm run build
```

## Test

```bash
npm test
```

## Notes

- If the backend services are not running, the page will load but API actions will fail.
- The frontend is intentionally minimal and focused on API coverage rather than UI design.
- Payment records are created by backend event flow, so you usually create an order first and then refresh payments.
