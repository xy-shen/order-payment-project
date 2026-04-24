# Kubernetes setup

This directory adds a simple Kubernetes deployment for the whole app:

- `mongo`
- `postgres`
- `zookeeper`
- `kafka`
- `order-service`
- `payment-service`
- `frontend`

The manifests intentionally keep the same internal hostnames used in Docker Compose, so the current Spring `docker` profile still works inside the cluster.

## Prerequisites

- A working Kubernetes cluster such as Docker Desktop Kubernetes, Minikube, or `kind`
- `kubectl`
- Docker images built into a registry or local cluster runtime

## Build the images

From the project root:

```bash
docker build -t final-project/order-service:latest -f order-service/Dockerfile .
docker build -t final-project/payment-service:latest -f payment-service/Dockerfile .
docker build -t final-project/frontend:latest -f frontend/Dockerfile .
```

If you are using a local cluster:

- Docker Desktop Kubernetes: the images above are usually enough.
- Minikube: run `eval $(minikube docker-env)` first, then build.
- `kind`: load the images with `kind load docker-image ...` after building.

## Deploy

```bash
kubectl apply -f k8s/app.yaml
kubectl get pods -n final-project
kubectl get svc -n final-project
```

## Open the app

If your cluster supports `LoadBalancer`, get the external address:

```bash
kubectl get svc frontend -n final-project
```

If it does not, port-forward instead:

```bash
kubectl port-forward svc/frontend 8080:80 -n final-project
```

Then open:

- `http://localhost:8080`

## Notes

- The backend services use the existing Spring `docker` profile.
- Mongo and Postgres use PVCs, so your cluster needs a default storage class.
- The frontend container proxies `/order-api` to `order-service` and `/payment-api` to `payment-service`, so the Angular app can keep using its current relative API paths.
- This is a development/demo Kubernetes setup, not a production-hardened deployment.
