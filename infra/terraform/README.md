# Terraform AWS Foundation

This folder provisions a minimal EKS-based AWS foundation for this demo project:

- VPC with the minimum public subnets EKS requires
- Internet gateway and public route table
- EKS cluster
- One managed node group
- ECR repositories for `order-service`, `payment-service`, and `frontend`
- CloudWatch log group for EKS control-plane logs

## Why This Is "Minimal"

This setup intentionally favors fewer moving parts over production hardening:

- public subnets only
- public EKS API endpoint
- one small managed node group by default
- no NAT gateway
- no private worker nodes

That keeps the Terraform simpler and cheaper, but it is not a production-grade Kubernetes platform.

## Cost Reality

This is the smallest practical EKS foundation for this repo, but it is still not a true free-tier setup.

Main reasons:

- EKS has a control-plane charge per cluster
- worker nodes are billed as EC2 instances
- EBS storage and data transfer may also add cost

If your main goal is to stay as close to free-tier as possible, ECS is usually the cheaper AWS path for this project.

## Defaults

The defaults are intentionally conservative:

- `2` public subnets because EKS requires subnets in at least two AZs
- `1` managed node by default
- `t3.medium` worker node type
- `7` day CloudWatch retention for control-plane logs

## Assumptions

- AWS is the target cloud provider.
- This is a development/demo-oriented setup, not a production-hardened platform.
- Terraform state is local for now. Move state to an S3 backend with locking before team use.
- You will build and push application images to ECR before applying the Kubernetes manifests.
- CloudWatch is enabled for EKS control-plane logs. Application pod logs will still need a log collector if you want them in CloudWatch too.
- The Kubernetes manifests in `/k8s` are the application layer that runs on top of this infrastructure.

## Files

- `versions.tf`: Terraform and provider requirements
- `variables.tf`: configurable inputs
- `main.tf`: AWS resources
- `outputs.tf`: useful values after apply
- `terraform.tfvars.example`: sample variable values

## Usage

1. Install Terraform, AWS CLI, `kubectl`, and configure AWS credentials.
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

6. Configure local `kubectl` access:

```bash
aws eks update-kubeconfig --region <aws-region> --name <cluster-name>
```

7. Build and push the application images to the ECR repositories from Terraform output.
8. Apply the Kubernetes manifests from the project root:

```bash
kubectl apply -f k8s/app.yaml
```

## What You Get After Apply

Terraform outputs:

- EKS cluster name
- EKS API endpoint
- managed node group name
- CloudWatch log group name
- VPC id
- public subnet ids
- ECR repository URLs for all three app images

## Important Note

EKS still requires subnets in at least two availability zones, so this Terraform keeps exactly that minimum even for a demo setup.

## Next Good Steps

- Push all three app images to ECR and update the Kubernetes image references
- Add a CloudWatch log collector for pod logs if you want application logs in CloudWatch
- Add resource requests and limits to the Kubernetes manifests
- Add readiness and liveness probes
- Move Terraform state to an S3 backend with locking
