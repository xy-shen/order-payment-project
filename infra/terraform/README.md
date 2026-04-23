# Terraform AWS Foundation

This folder provisions a low-cost AWS foundation for this demo project:

- VPC with public and private subnets
- Internet gateway and route tables
- ECR repositories for `order-service` and `payment-service`
- ECS cluster
- One ECS-optimized EC2 host instance
- S3 bucket for Angular frontend build artifacts
- CloudFront distribution for frontend delivery

## Why ECS Instead Of EKS

This project is a demo with minimal load, so cost matters more than Kubernetes features right now.

AWS currently documents:

- Amazon EKS standard-support clusters cost `$0.10/hour` per cluster
- Amazon ECS with the EC2 launch type has no additional ECS charge beyond the EC2 resources you run

Sources:

- [Amazon EKS pricing](https://aws.amazon.com/eks/pricing/)
- [Amazon ECS pricing](https://aws.amazon.com/ecs/pricing/)

Because of that, this Terraform now uses `ECS + one small EC2 instance` instead of EKS.

## Defaults

The defaults are tuned for low cost:

- `1` availability zone
- `t3.micro` ECS host
- one public EC2 instance instead of a NAT-backed private cluster
- static frontend on S3 + CloudFront

## Assumptions

- AWS is the target cloud provider.
- This is a development/demo-oriented setup, not a production-hardened platform.
- Terraform state is local for now. Move state to an S3 backend with locking before team use.
- The frontend is hosted as static files on S3 behind CloudFront.
- Backend containers will be pushed to ECR and then run on the ECS host later.

## Files

- `versions.tf`: Terraform and provider requirements
- `variables.tf`: configurable inputs
- `main.tf`: AWS resources
- `outputs.tf`: useful values after apply
- `terraform.tfvars.example`: sample variable values

## Usage

1. Install Terraform and configure AWS credentials.
2. Copy the example vars file:

```bash
cp terraform.tfvars.example terraform.tfvars
```

3. Review and adjust values in `terraform.tfvars`.
4. Initialize and preview the plan:

```bash
terraform init
terraform plan
```

5. Apply when ready:

```bash
terraform apply
```

## What You Get After Apply

Terraform outputs:

- ECS cluster name
- ECS host EC2 instance id
- ECS host public IP
- ECR repository URLs for both backend services
- frontend S3 bucket name
- frontend CloudFront domain and URL

## Frontend Deploy Flow

The Angular source stays in `frontend/`, but the built files should be uploaded to the Terraform-managed S3 bucket after `npm run build`.

Typical flow:

```bash
cd frontend
npm install
npm run build
```

Then upload the generated `dist` output to the S3 bucket from Terraform output.

## Backend Deploy Direction

This Terraform creates the base ECS cluster and one ECS-capable EC2 host, but it does not yet define ECS task definitions or services for `order-service` and `payment-service`.

That is intentional so we can keep the infrastructure cheap and simple first.

## Important Note

The current frontend uses local Angular proxy paths for development. Before production deployment, you will likely want to add environment-based API URLs so the built frontend can call your AWS-hosted backend services directly instead of relying on the local dev proxy.

## Next Good Steps

- Add an S3 backend and state locking
- Add ECS task definitions and services for the backend containers
- Add a deployment step that uploads Angular build artifacts to the frontend bucket
- Add a reverse proxy or load balancer only if the demo needs a cleaner public entrypoint
- Replace direct public-instance access with a more production-like topology later if needed
