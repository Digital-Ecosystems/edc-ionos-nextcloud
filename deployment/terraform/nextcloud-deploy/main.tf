provider "helm" {
  kubernetes {
    config_path = "${var.kubeconfig}"
  }
}

variable "kubeconfig" {
  type = string
}

variable "namespace" {
  default = "edc-ionos-nextcloud"
}

variable "ids_webhook_address" {
  default = "http://localhost:8282"
}

variable "image_repository" {
  type = string
  default = "ghcr.io/digital-ecosystems/nextcloud-connector"
}

variable "image_tag" {
  type = string
  default = ""
}

variable "pg_host" {
  type = string
  default = "localhost"
}

variable "pg_port" {
  type = number
  default = 5432
}

variable "pg_database" {
  type = string
  default = "postgres"
}

variable "pg_username" {
  type = string
  default = "postgres"
}

variable "pg_password" {
  type = string
  default = "postgres"
}



variable "vaultname" {
  default = "vault"
}

variable "nextcloud_endpoint" {
  type = string
  default = "http://localhost:8080"
}

variable "nextcloud_username" {
  type = string
  default = "admin"
}

variable "nextcloud_password" {
  type = string
  default = "password"
}

locals {
  vault_token = fileexists("../vault-init/vault-tokens.json") ? "${jsondecode(file("../vault-init/vault-tokens.json")).auth.client_token}" : ""
}

resource "helm_release" "edc-ionos-nextcloud" {
  name       = "edc-ionos-nextcloud"

  repository = "../../helm"
  chart      = "edc-ionos-nextcloud"

  namespace = var.namespace
  create_namespace = true

  set {
    name  = "edc.vault.hashicorp.token"
    value = local.vault_token
  }

  values = [
    "${file("../../helm/edc-ionos-nextcloud/values.yaml")}",
  ]

  set {
    name  = "edc.vault.hashicorp.url"
    value = "http://${var.vaultname}:8200"
  }

  set {
    name  = "edc.ionos.nextcloud.endpoint"
    value = var.nextcloud_endpoint
  }

  set {
    name  = "edc.ionos.nextcloud.username"
    value = var.nextcloud_username
  }
  set {
      name  = "edc.ionos.nextcloud.password"
      value = var.nextcloud_password
    }

  set {
    name  = "ids.webhook.address"
    value = var.ids_webhook_address
  }

  set {
    name  = "edc.postgresql.host"
    value = var.pg_host
  }

  set {
    name  = "edc.postgresql.database"
    value = var.pg_database
  }

  set {
    name  = "edc.postgresql.port"
    value = var.pg_port
  }

  set {
    name  = "edc.postgresql.username"
    value = var.pg_username
  }

  set {
    name  = "edc.postgresql.password"
    value = var.pg_password
  }

  set {
    name = "image.repository"
    value = var.image_repository
  }

  set {
    name = "image.tag"
    value = var.image_tag
  }
}