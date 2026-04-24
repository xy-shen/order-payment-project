output "aws_region" {
  description = "AWS region for the provisioned infrastructure."
  value       = var.aws_region
}

output "eks_cluster_name" {
  description = "EKS cluster name."
  value       = aws_eks_cluster.main.name
}

output "eks_cluster_arn" {
  description = "EKS cluster ARN."
  value       = aws_eks_cluster.main.arn
}

output "eks_cluster_endpoint" {
  description = "Public EKS API server endpoint."
  value       = aws_eks_cluster.main.endpoint
}

output "eks_cluster_certificate_authority_data" {
  description = "Base64-encoded certificate data for the EKS cluster."
  value       = aws_eks_cluster.main.certificate_authority[0].data
}

output "eks_node_group_name" {
  description = "Managed node group name."
  value       = aws_eks_node_group.main.node_group_name
}

output "cloudwatch_log_group_name" {
  description = "CloudWatch log group for EKS control-plane logs."
  value       = aws_cloudwatch_log_group.eks_cluster.name
}

output "vpc_id" {
  description = "VPC identifier."
  value       = aws_vpc.main.id
}

output "public_subnet_ids" {
  description = "Public subnet IDs."
  value       = [for subnet in aws_subnet.public : subnet.id]
}

output "order_service_repository_url" {
  description = "ECR repository URL for the order service image."
  value       = aws_ecr_repository.order_service.repository_url
}

output "payment_service_repository_url" {
  description = "ECR repository URL for the payment service image."
  value       = aws_ecr_repository.payment_service.repository_url
}

output "frontend_repository_url" {
  description = "ECR repository URL for the frontend image."
  value       = aws_ecr_repository.frontend.repository_url
}
