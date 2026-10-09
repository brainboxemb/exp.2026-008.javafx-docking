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
$downloadDir = Join-Path $root ".deps\downloads"

$snapFxVersion = "0.8.0-0"
$snapFxRelease = "v0.8.0"
$snapFxJarName = "snapfx-core-$snapFxVersion.jar"
$snapFxJarUrl = "https://github.com/Beowolve/SnapFX/releases/download/$snapFxRelease/$snapFxJarName"
$snapFxSha256 = "77c16fe87e795762aea4be0abb4ada12782503be1c9511c46b3e11b765354b3b"

$downloadedJar = Join-Path $downloadDir $snapFxJarName
$bootstrapPom = Join-Path $root "bootstrap\snapfx-core-0.8.0-0.pom"

$mavenArtifactDir = Join-Path $HOME ".m2\repository\org\snapfx\snapfx-core\$snapFxVersion"
$mavenJar = Join-Path $mavenArtifactDir $snapFxJarName
$mavenPom = Join-Path $mavenArtifactDir "snapfx-core-$snapFxVersion.pom"

New-Item -ItemType Directory -Force -Path $downloadDir | Out-Null

Write-Host "Downloading SnapFX $snapFxRelease release JAR..."
Invoke-WebRequest -Uri $snapFxJarUrl -OutFile $downloadedJar

$actualSha256 = (Get-FileHash -Path $downloadedJar -Algorithm SHA256).Hash.ToLowerInvariant()
if ($actualSha256 -ne $snapFxSha256) {
    throw "SnapFX SHA-256 mismatch. Expected $snapFxSha256 but got $actualSha256"
}

if (-not (Test-Path $bootstrapPom)) {
    throw "Bootstrap POM missing: $bootstrapPom"
}

$mavenWrapper = if ($IsWindows) {
    Join-Path $root "mvnw.cmd"
} else {
    Join-Path $root "mvnw"
}

if (-not (Test-Path $mavenWrapper)) {
    throw "Maven Wrapper missing: $mavenWrapper"
}

Write-Host "Installing verified SnapFX JAR in Maven local..."
Invoke-Native -Command $mavenWrapper -Arguments @(
    "-B",
    "-ntp",
    "org.apache.maven.plugins:maven-install-plugin:3.1.4:install-file",
    "-Dfile=$downloadedJar",
    "-DpomFile=$bootstrapPom"
)

if (-not (Test-Path $mavenJar)) {
    throw "SnapFX JAR was not installed in Maven local: $mavenJar"
}
if (-not (Test-Path $mavenPom)) {
    throw "SnapFX POM was not installed in Maven local: $mavenPom"
}

Write-Host "SnapFX $snapFxRelease is available in Maven local."
Write-Host $mavenArtifactDir
