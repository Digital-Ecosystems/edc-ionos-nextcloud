#!/bin/bash

# Check if all requirements are installed
if ! command -v helm &> /dev/null
then
    echo "helm could not be found"
    exit
fi

if ! command -v kind &> /dev/null
then
    echo "kind could not be found"
    exit
fi

if ! command -v kubectl &> /dev/null
then
    echo "kubectl could not be found"
    exit
fi

if ! command -v docker &> /dev/null
then
    echo "docker could not be found"
    exit
fi

# Check for environment variables
if [ -z `printenv NEXTCLOUD_ENPOINT` ]; then
    echo "Stopping because NEXTCLOUD_ENPOINT is undefined"
    exit 1
fi 
if [ -z `printenv NEXTCLOUD_USERNAME` ]; then
    echo "Stopping because NEXTCLOUD_USERNAME is undefined"
    exit 1
fi 
if [ -z `printenv NEXTCLOUD_PASSWORD` ]; then
    echo "Stopping because NEXTCLOUD_PASSWORD is undefined"
    exit 1
fi


# clean old installation
scripts/cleanup.sh

# Create a kind cluster
kind create cluster --name edc-ionos-nextcloud
kubectl apply -f ./metalLB/metalLB-native.yaml
kubectl wait --for=condition=available --timeout=600s deployment -n metallb-system controller
kubectl apply -f ./metalLB/metalLB.yaml
kubectl create namespace edc-ionos-nextcloud

# Build docker image
cd ../../
./gradlew clean build
docker build -t ghcr.io/edc-ionos-nextcloud/connector:1.0.0 ./connector/
cd ./deployment/kind/
kind load docker-image ghcr.io/edc-ionos-nextcloud/connector:1.0.0 --name edc-ionos-nextcloud

# Deploy Vault
helm repo add hashicorp https://helm.releases.hashicorp.com
helm install -n edc-ionos-nextcloud --wait vault hashicorp/vault \
    -f ./scripts/vault-values.yaml \
    --version 0.19.0 \
    --create-namespace \
    --kubeconfig=$KUBECONFIG

# Init Vault
export TF_VAR_kubeconfig=$KUBECONFIG
export TF_VAR_s3_access_key=$S3_ACCESS_KEY
export TF_VAR_s3_secret_key=$S3_SECRET_KEY
export TF_VAR_s3_endpoint_region=$S3_ENDPOINT_REGION
export TF_VAR_ionos_token=$IONOS_TOKEN
../terraform/vault-init/vault-init.sh

# Deploy IONOS-S3
helm install -n edc-ionos-nextcloud --wait edc-ionos-nextcloud ../helm/edc-ionos-nextcloud \
    -f ./scripts/edc-s3-values.yaml \
    --create-namespace \
    --set edc.vault.hashicorp.token=$(jq -r .root_token ./vault-keys.json) \
    --kubeconfig=$KUBECONFIG

echo "$(kubectl get svc -n edc-ionos-nextcloud edc-ionos-nextcloud -o jsonpath='{.status.loadBalancer.ingress[0].ip}') edc-ionos-nextcloud-service" | sudo tee -a /etc/hosts
echo "$(kubectl get svc -n edc-ionos-nextcloud vault-ui -o jsonpath='{.status.loadBalancer.ingress[0].ip}') vault-service" | sudo tee -a /etc/hosts

echo "----------------------------------"

# EDC Ionos S3 service address
echo "API URL: http://edc-ionos-nextcloud-service:8181"
echo "Management URL: http://edc-ionos-nextcloud-service:8182"
echo "IDS URL: http://edc-ionos-nextcloud-service:8282"

echo "----------------------------------"

# Vault service address
echo "Vault URL: http://vault-service:8200"
