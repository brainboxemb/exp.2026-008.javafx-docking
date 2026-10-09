$ErrorActionPreference = "Stop"

function Invoke-Native {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Command,

        [Parameter(ValueFromRemainingArguments = $true)]
        [string[]]$Arguments
    )

    & $Command @Arguments
    if ($LASTEXITCODE -ne 0) {
        throw "$Command failed with exit code $LASTEXITCODE"
    }
}

$root = $PSScriptRoot
$snapFxDir = Join-Path $root ".deps\SnapFX"
$snapFxRepository = "https://github.com/Beowolve/SnapFX.git"
$snapFxTag = "v0.8.0"
$snapFxCommit = "6253f6443c74718b2bb8f861835dc97c6f5e374f"
$mavenArtifactDir = Join-Path $HOME ".m2\repository\org\snapfx\snapfx-core\0.8.0-0"
$mavenJar = Join-Path $mavenArtifactDir "snapfx-core-0.8.0-0.jar"
$mavenPom = Join-Path $mavenArtifactDir "snapfx-core-0.8.0-0.pom"

if (-not (Test-Path (Join-Path $snapFxDir ".git"))) {
    New-Item -ItemType Directory -Force -Path (Split-Path $snapFxDir) | Out-Null
    Invoke-Native git clone --no-checkout $snapFxRepository $snapFxDir
}

Invoke-Native git -C $snapFxDir fetch origin ("refs/tags/" + $snapFxTag + ":refs/tags/" + $snapFxTag) --force
Invoke-Native git -C $snapFxDir checkout --detach $snapFxCommit

$actualCommit = (git -C $snapFxDir rev-parse HEAD).Trim()
if ($LASTEXITCODE -ne 0) {
    throw "Unable to read SnapFX commit"
}
if ($actualCommit -ne $snapFxCommit) {
    throw "Unexpected SnapFX source: $actualCommit"
}

$gradleWrapper = Join-Path $snapFxDir "gradlew.bat"
Invoke-Native $gradleWrapper --no-daemon --project-dir $snapFxDir :snapfx-core:publishToMavenLocal

if (-not (Test-Path $mavenJar)) {
    throw "SnapFX JAR was not installed in Maven local: $mavenJar"
}
if (-not (Test-Path $mavenPom)) {
    throw "SnapFX POM was not installed in Maven local: $mavenPom"
}

Write-Host "SnapFX $snapFxTag is available in Maven local."
Write-Host $mavenArtifactDir
