param(
    [switch]$VerifyOnly
)

$ErrorActionPreference = "Stop"

function Invoke-Native {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Command,

        [Parameter(Mandatory = $true)]
        [string[]]$Arguments
    )

    & $Command @Arguments
    if ($LASTEXITCODE -ne 0) {
        throw "$Command failed with exit code $LASTEXITCODE"
    }
}

$root = $PSScriptRoot

& (Join-Path $root "bootstrap.ps1")

Push-Location $root
try {
    Invoke-Native -Command "mvn" -Arguments @(
        "-B",
        "-ntp",
        "verify"
    )

    if (-not $VerifyOnly) {
        Invoke-Native -Command "mvn" -Arguments @(
            "javafx:run"
        )
    }
}
finally {
    Pop-Location
}
