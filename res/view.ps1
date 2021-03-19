$sd = "out"
$f = "results.csv"
$cd = Get-Location
$p =  "csvfileview_64.exe"

 #Push-Location  $(Get-Item $($(Get-Item  $(Get-Location)).Parent).FullName)
 #$s = ((Get-Location | Get-ChildItem -Filter $p -Recurse).FullName) |

 #Select-Object -First 1
 #$(Join-Path "out" $f)
 #$f = ((Get-Location | Get-ChildItem -Filter $p -Recurse).FullName) | Select-Object -First 1
Push-Location out
 if((Test-Path $p)){
        if(!(Test-Path $f)){
             $f = Get-FileName(pwd)
        }
        Start-Process -FilePath $p -ArgumentList $f
 }

Pop-Location

Function Get-FileName($initialDirectory)
{
 [System.Reflection.Assembly]::LoadWithPartialName("System.windows.forms") |
 Out-Null

 $OpenFileDialog = New-Object System.Windows.Forms.OpenFileDialog
 $OpenFileDialog.initialDirectory = $initialDirectory
  $OpenFileDialog.filter = "Comma Separated Data (*.csv)| *.csv"
  #$OpenFileDialog.filter = "All files (*.*)| *.*"
 $OpenFileDialog.ShowDialog() | Out-Null
 $OpenFileDialog.filename
} #end