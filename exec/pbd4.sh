#!/bin/bash

##########################################################################
# This script runs PBD4 from its modular jar.                            #
#                                                                        #
# If PBD4 and its dependencies are not stored in the default directory,  #
# you must specify their folder using:                                   #
#       export PBD4_HOME="/path/to/pbd4"                                 #
#                                                                        #
# If options must be passed to the Java Virtual Machine (and NOT to      #
# PBD4), you must specify them by exporting either JAVA_OPTS or          #
# JVM_ARGS set with the appropriate value.                               #
##########################################################################

if [ -z "$PBD4_HOME" ]
then
    # Using the default directory for PBD4.
    PBD4_HOME="dist/jars"
fi

if [ ! -d "$PBD4_HOME" ]
then
    # PBD4 directory does not exist.
    echo "$0: $PBD4_HOME: No such file or directory"
    exit 127
fi

# Running PBD4.
java $JAVA_OPTS $JVM_ARGS \
    -p "$PBD4_HOME" \
    -m fr.univartois.cril.pbd4/fr.univartois.cril.pbd4.D4Launcher \
    $@
