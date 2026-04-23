variable "aws_region" {
  description = "AWS region for the infrastructure."
  type        = string
  default     = "us-west-2"
}

variable "project_name" {
  description = "Project name used as the resource naming prefix."
  type        = string
  default     = "final-project"
}

variable "environment" {
  description = "Deployment environment name."
  type        = string
  default     = "dev"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC."
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zone_count" {
  description = "Number of availability zones to use."
  type        = number
  default     = 1

  validation {
    condition     = var.availability_zone_count >= 1 && var.availability_zone_count <= 2
    error_message = "availability_zone_count must be between 1 and 2."
  }
}

variable "ec2_instance_type" {
  description = "EC2 instance type for the ECS host."
  type        = string
  default     = "t3.micro"
}

variable "ec2_root_volume_size" {
  description = "Root EBS volume size in GiB for the ECS host."
  type        = number
  default     = 32
}

variable "app_ingress_ports" {
  description = "Public TCP ports to open on the ECS host for demo applications."
  type        = list(number)
  default     = [80, 8081, 8082]
}

variable "app_ingress_cidrs" {
  description = "CIDR blocks allowed to access demo application ports."
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "ssh_ingress_cidrs" {
  description = "CIDR blocks allowed to SSH into the ECS host."
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "frontend_bucket_force_destroy" {
  description = "Whether to allow Terraform to delete the frontend bucket even if it contains files."
  type        = bool
  default     = false
}

variable "frontend_price_class" {
  description = "CloudFront price class for the frontend distribution."
  type        = string
  default     = "PriceClass_100"

  validation {
    condition = contains(
      ["PriceClass_100", "PriceClass_200", "PriceClass_All"],
      var.frontend_price_class
    )
    error_message = "frontend_price_class must be PriceClass_100, PriceClass_200, or PriceClass_All."
  }
}

variable "tags" {
  description = "Extra tags to apply to all supported resources."
  type        = map(string)
  default     = {}
}
