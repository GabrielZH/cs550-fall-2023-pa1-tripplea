JFLAGS = -g
JC = javac
JVM= java
MAIN = Peer CentralIndexingServer
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        CentralIndexingServer.java \
        Peer.java \
        GetParameter.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
