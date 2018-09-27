# WinRM Checker #
Java utility for interacting with WinRM interface.

## Features ##
- HTTP Basic authentication
- Running CMD commands
- Running PS commands **(not implemented yet)**
- Simple STDOUT logging
- HTTP support
- HTTPS support **(should be working, not tested yet)**

## Building ##
- Have at least Java 8.
- Clone the repository.
- Import existing maven project into your IDE.
- Run/debug the project (if you want to hack on it).
- Use your IDE capabilities to export runnable jar file. (For example *File -> Export -> Runnable JAR file* in Eclipse.) **The "maven package" currently does not produce working JAR because of missing CXF integration in the project POM.**

## Usage ##
```
java -jar winrm-checker.jar
Unable to parse command line arguments.
Missing required options: h, p, u, w

usage: java -jar winrm-checker-jar-with-dependencies.jar
 -a <arg>   Authentication method. Default: Basic. Implemented: Basic.
 -c <arg>   Non-PS command to execute. Default: Winrm id
 -h <arg>   Target hostname or IP address.
 -k         Ignore HTTPS certificate problems.
 -p <arg>   Target port.
 -s         Use HTTPS.
 -u <arg>   Username.
 -w <arg>   Password.
```

## Example ##
```
java -jar winrm-checker.jar -p 5985 -u Administrator -w ***** -h 172.31.255.180
...
Thu Sep 27 16:14:41 CEST 2018 [INFO]: Response STDOUT:
IdentifyResponse
    ProtocolVersion = http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd
    ProductVendor = Microsoft Corporation
    ProductVersion = OS: 6.3.9600 SP: 0.0 Stack: 3.0
    SecurityProfiles
        SecurityProfileName = http://schemas.dmtf.org/wbem/wsman/1/wsman/secprofile/http/basic, http://schemas.dmtf.org/wbem/wsman/1/wsman/secprofile/http/spnego-kerberos
...
```
