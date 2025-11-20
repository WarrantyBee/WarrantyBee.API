Clear-Host
Write-Host "====================================="
Write-Host "   WarrantyBee API Deployment Tool   "
Write-Host "====================================="
Write-Host ""
Write-Host "Select Environment:"
Write-Host "1) Test"
Write-Host "2) Stage"
Write-Host "3) Production"
Write-Host ""

$selection = Read-Host "Enter your choice (1-3)"

switch ($selection) {
    "1" {
        $envName = "Test"
        $env = "test"
    }
    "2" {
        $envName = "Stage"
        $env = "stage"
    }
    "3" {
        $envName = "Production"
        $env = "prod"
    }
    default {
        Write-Host "Invalid selection. Exiting." -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
Write-Host "Selected Environment: $envName"
Write-Host ""

# ============================================
# VERSION INPUT
# ============================================
$newVersion = Read-Host "Enter version (e.g., v1.0.3)"

if (-not $newVersion) {
    Write-Host "Version is required. Exiting." -ForegroundColor Red
    exit 1
}

# Timestamp tag
$timestamp = (Get-Date -Format "yyyyMMddHHmmss")

# ============================================
# BUILD DOCKER IMAGE
# ============================================
$imageName = "warrantybee-api-$env"
$dockerLocalTag = "${imageName}:latest"

Write-Host ""
Write-Host "Building Docker image..."
docker build -t $dockerLocalTag .

if ($LASTEXITCODE -ne 0) {
    Write-Host "Docker build failed. Exiting." -ForegroundColor Red
    exit 1
}

# ============================================
# DOCKER HUB TAGS
# ============================================
$dockerHubVersionTag = "arijitroy/${imageName}:${newVersion}"
$dockerHubTimestampTag = "arijitroy/${imageName}:${timestamp}"

Write-Host "Tagging Docker Hub images..."
docker tag $dockerLocalTag $dockerHubVersionTag
docker tag $dockerLocalTag $dockerHubTimestampTag

# ============================================
# GHCR TAGS
# ============================================
$ghcrVersionTag = "ghcr.io/arijitroy/${imageName}:${newVersion}"
$ghcrTimestampTag = "ghcr.io/arijitroy/${imageName}:${timestamp}"

Write-Host "Tagging GHCR images..."
docker tag $dockerLocalTag $ghcrVersionTag
docker tag $dockerLocalTag $ghcrTimestampTag

# ============================================
# PUSH ALL TAGS
# ============================================
Write-Host ""
Write-Host "Pushing to Docker Hub..."
docker push $dockerHubVersionTag
docker push $dockerHubTimestampTag

Write-Host ""
Write-Host "Pushing to GHCR..."
docker push $ghcrVersionTag
docker push $ghcrTimestampTag

Write-Host ""
Write-Host "====================================="
Write-Host "Deployment Completed Successfully!"
Write-Host "Environment: $envName"
Write-Host "Version:     $newVersion"
Write-Host "Timestamp:   $timestamp"
Write-Host "====================================="
