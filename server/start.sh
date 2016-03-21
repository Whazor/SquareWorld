#!/bin/bash
cd "$( dirname "$0" )"
rm -rf world*/
java -Xmx1024M -jar craftbukkit.jar -o true
