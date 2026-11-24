### Chat client that sends and receives sound/voice
Sample sound/voice from computer microphone
to send voice message using javax.sound.sampled.

Application functionality:
- records sound
- sends sound with TCP
- receives sound with TCP
- play received sound

Server 2.1.5-server stored locally only.
Server handles type/object Storage (implements Serializable).



YouTube tutorials:
https://www.youtube.com/watch?v=GVtl19L9GxU&t=809s
https://www.youtube.com/watch?v=TprSF6EAmUA
https://www.youtube.com/watch?v=WSyTrdjKeqQ&t=623s

Interface SourceDataLine used to play received sound.
https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/SourceDataLine.html

Interface Line.
https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/Line.html#close--

javax.sound.sampled
https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/package-summary.html