#!/bin/sh

mvn install exec:java -Dexec.mainClass="it.jugpadova.jugevents.batch.EncryptExistingPassword"
