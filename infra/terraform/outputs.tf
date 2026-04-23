output "aws_region" {
  description = "AWS region for the provisioned infrastructure."
  value       = var.aws_region
}

output "ecs_cluster_name" {
  description = "ECS cluster name."
  value       = aws_ecs_cluster.main.name
}

output "ecs_host_instance_id" {
  description = "EC2 instance id for the ECS host."
  value       = aws_instance.ecs_host.id
}

output "ecs_host_public_ip" {
  description = "Public IP address of the ECS host."
  value       = aws_instance.ecs_host.public_ip
}

output "vpc_id" {
  description = "VPC identifier."
  value       = aws_vpc.main.id
}

output "public_subnet_ids" {
  description = "Public subnet IDs."
  value       = [for subnet in aws_subnet.public : subnet.id]
}

output "private_subnet_ids" {
  description = "Private subnet IDs."
  value       = [for subnet in aws_subnet.private : subnet.id]
}

output "order_service_repository_url" {
  description = "ECR repository URL for the order service image."
  value       = aws_ecr_repository.order_service.repository_url
}

output "payment_service_repository_url" {
  description = "ECR repository URL for the payment service image."
  value       = aws_ecr_repository.payment_service.repository_url
}

output "frontend_bucket_name" {
  description = "S3 bucket that stores the frontend build artifacts."
  value       = aws_s3_bucket.frontend.bucket
}

output "frontend_cloudfront_domain_name" {
  description = "CloudFront domain name for the frontend."
  value       = aws_cloudfront_distribution.frontend.domain_name
}

output "frontend_url" {
  description = "HTTPS URL for the frontend distribution."
  value       = "https://${aws_cloudfront_distribution.frontend.domain_name}"
}
