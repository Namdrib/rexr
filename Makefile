# Makefile for rexr

JFLAGS = -Xlint -Xdiags:verbose

all: rexr

main: rexr

rexr: Recorder.class rexr.java
	javac rexr.java $(JFLAGS)

Recorder.class: Recorder.java
	javac Recorder.java $(JFLAGS)

clean:
	rm -f *.class