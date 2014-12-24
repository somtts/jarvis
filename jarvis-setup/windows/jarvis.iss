; -- jarvis.iss --
; To successfully run this installation and the program it installs,
; you must have a "x64" edition of Windows.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=Jarvis
AppVersion=1.0
DefaultDirName={pf}\Jarvis
DefaultGroupName=Jarvis
UninstallDisplayIcon={app}\uninstall-jarvis.exe
Compression=lzma2
SolidCompression=yes
OutputDir=userdocs:Inno Setup Examples Output
; "ArchitecturesAllowed=x64" specifies that Setup cannot run on
; anything but x64.
ArchitecturesAllowed=x64
; "ArchitecturesInstallIn64BitMode=x64" requests that the install be
; done in "64-bit mode" on x64, meaning it should use the native
; 64-bit Program Files directory and the 64-bit view of the registry.
ArchitecturesInstallIn64BitMode=x64

[Files]
Source: "../../jarvis-core/jarvis-voice/target/jarvis-voice-0.0.1-SNAPSHOT-jar-with-dependencies.jar"; DestDir: "{app}/java"; DestName: "jarvis-voice-0.0.1-SNAPSHOT.jar"
; node.exe
Source: "../../jarvis-core/jarvis-nodejs/node/*"; DestDir: "{app}/node"; Flags: recursesubdirs
; TODO blammo.xml injection
; TODO cert https injection
; NodeJS
Source: "../../jarvis-core/jarvis-nodejs/src/main/nodejs/*"; DestDir: "{app}/nodejs"; Flags: recursesubdirs
; Windows clients
Source: "../../jarvis-windows/WindowsJarvisMicrophone/bin/Debug/*.exe"; DestDir: "{app}/windows"
Source: "../../jarvis-windows/WindowsJarvisMicrophone/bin/Debug/*.dll"; DestDir: "{app}/windows"
; Win-bash
Source: "../../jarvis-core/jarvis-nodejs/src/main/shell.w32-ix86/*"; DestDir: "{app}/scripts/shell.w32-ix86"; Flags: recursesubdirs
; Scripts
Source: "../../jarvis-core/jarvis-nodejs/src/main/jarvis.sh"; DestDir: "{app}/scripts";
Source: "../../jarvis-core/jarvis-nodejs/src/main/jarvis-bootstrap.cmd"; DestDir: "{app}/scripts";

[Icons]
Name: "{group}\Jarvis"; Filename: "{app}\jarvis.exe"

[Registry]
Root: HKCU; Subkey: "Software\Jarvis"; Flags: uninsdeletekeyifempty
Root: HKCU; Subkey: "Software\Jarvis\Commons"; Flags: uninsdeletekey
Root: HKLM; Subkey: "Software\Jarvis"; Flags: uninsdeletekeyifempty
Root: HKLM; Subkey: "Software\Jarvis\Commons"; Flags: uninsdeletekey
Root: HKLM; Subkey: "Software\Jarvis\Commons\Settings"; ValueType: string; ValueName: "InstallPath"; ValueData: "{app}"
