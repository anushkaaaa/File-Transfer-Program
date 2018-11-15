# File-Transfer-Program
This project is intended to reinforce the principles of the inter-process communication and the end-to-end argument. The aim is to develop a simplified version of a ftp program (similar to the one described in the end-to-end argument paper) using the stream sockets available in java.net package.

By: Anushka H Patil
-------------------------------

Content:
1) Client.java
2) Server.java
3) Files/ directory containing all the files to be download
4) user.txt
5) Makefile
6) Report
7) readme

------------------------------
To compile the file: make<br />
My server is running on in-csci-rrpc01.cs.iupui.edu 10.234.136.55<br />
So Open an instance of putty on the above machine<br /><br />
To run the server: java Server<br />

Client can run on all of the below machines<br />
So Open an instance of putty on any of the machines<br />
To run the client: java Client<br />

Since, I have designed a multithreaded program it supports number of clients at same time. You can open another instance of putty and run another client using same: java Client.

------------------------------

The program runs on any machine from the following list: <br />
in-csci-rrpc01.cs.iupui.edu 10.234.136.55 <br />
in-csci-rrpc02.cs.iupui.edu 10.234.136.56 <br />
in-csci-rrpc03.cs.iupui.edu 10.234.136.57 <br />
in-csci-rrpc04.cs.iupui.edu 10.234.136.58 <br />
in-csci-rrpc05.cs.iupui.edu 10.234.136.59 <br />
in-csci-rrpc06.cs.iupui.edu 10.234.136.60 <br />

-----------------------------
Note:
For every new client my file system will create a new directory for every client and this directory will contains the files successfully transmitted from server to client.
So, for example: Client: abc Files downloaded: file1.txt, file2.txt<br />
abc/<br /><br />
	file1.txt<br />
	file2.txt<br />

-----------------------------

Server Files:<br />
file1.txt<br />
file2.txt<br />
file3.txt<br />

----------------------------
My port number is 4004, in case there is Server Error please change the port number and try some other port number.
