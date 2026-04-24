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

variable "eks_public_access_cidrs" {
  description = "CIDR blocks allowed to access the public EKS API endpoint."
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "eks_node_instance_types" {
  description = "EC2 instance types for the EKS managed node group."
  type        = list(string)
  default     = ["t3.medium"]
}

variable "eks_node_disk_size" {
  description = "Disk size in GiB for each EKS worker node."
  type        = number
  default     = 20
}

variable "eks_node_desired_size" {
  description = "Desired number of EKS worker nodes."
  type        = number
  default     = 1
}

variable "eks_node_min_size" {
  description = "Minimum number of EKS worker nodes."
  type        = number
  default     = 1
}

variable "eks_node_max_size" {
  description = "Maximum number of EKS worker nodes."
  type        = number
  default     = 1
}

variable "eks_enabled_cluster_log_types" {
  description = "EKS control-plane log types to send to CloudWatch."
  type        = list(string)
  default     = ["api", "audit", "authenticator"]

  validation {
    condition = alltrue([
      for log_type in var.eks_enabled_cluster_log_types :
      contains(["api", "audit", "authenticator", "controllerManager", "scheduler"], log_type)
    ])
    error_message = "eks_enabled_cluster_log_types contains an unsupported log type."
  }
}

variable "cloudwatch_log_retention_days" {
  description = "Retention period in days for CloudWatch log groups."
  type        = number
  default     = 7
}

variable "tags" {
  description = "Extra tags to apply to all supported resources."
  type        = map(string)
  default     = {}
}
