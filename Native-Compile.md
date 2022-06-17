### Compile C using OpenWhisk and Docker

1. Run the latest version of the OpenWhisk Skeleton </br>
`docker run -d --name "openwhisk" openwhisk/dockerskeleton:latest`
2. Copy necessary files from host computer to Docker image </br>
`docker cp <file-name> openwhisk:/action/`
3. Enter the running Docker container </br>
`docker exec -it openwhisk bash`
4. Install the build tools </br>
`apk add build-base`
5. Change to the correct directory </br>
`cd /action`
6. Compile! </br>
`gcc <file-name>.c -o <file-name>`
7. Change permissions </br>
`chmod 755 <file-name>`
8. Exit container </br>
`exit`
9. Copy the file back to host system </br>
`docker cp openwhisk/<file-name> <file-name>`
10. Rename file (has to be exec) </br>
`mv <file-name> exec`
11. Zip it up </br>
`zip action.zip exec`
12. Deploy it </br>
`ibmcloud fn action create my_action action.zip --native`
13. Test </br>
`ibmcloud fn action invoke my_action -r`
