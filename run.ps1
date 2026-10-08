param(
    [switch]$VerifyOnly
)

$ErrorActionPreference = "Stop"

$root = $PSScriptRoot
$snapFxDir = Join-Path $root ".deps\SnapFX"
$snapFxRepository = "https://github.com/Beowolve/SnapFX.git"
$snapFxTag = "v0.8.0"
$snapFxCommit = "6253f6443c74718b2bb8f861835dc97c6f5e374f"

if (-not (Test-Path (Join-Path $snapFxDir ".git"))) {
    New-Item -ItemType Directory -Force -Path (Split-Path $snapFxDir) | Out-Null
    git clone --no-checkout $snapFxRepository $snapFxDir
}

git -C $snapFxDir fetch origin ("refs/tags/" + $snapFxTag + ":refs/tags/" + $snapFxTag) --force
git -C $snapFxDir checkout --detach $snapFxCommit

$actualCommit = (git -C $snapFxDir rev-parse HEAD).Trim()
if ($actualCommit -ne $snapFxCommit) {
    throw "Unexpected SnapFX source: $actualCommit"
}

& (Join-Path $snapFxDir "gradlew.bat") --no-daemon --project-dir $snapFxDir :snapfx-core:publishToMavenLocal

Push-Location $root
try {
    mvn -B -ntp verify

    if (-not $VerifyOnly) {
        mvn javafx:run
    }
}
finally {
    Pop-Location
}
