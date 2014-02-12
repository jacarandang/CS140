from random import randint, choice

fname = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l']

f = file("tasklist.txt", "w")

n = 10

t = 1
for i in xrange(n):
	t += randint(0, 5)
	f.write(fname[i]+" "+str(t)+"\n")
	
f.close()

for i in xrange(n):
	f = file(fname[i]+".txt", "w")
	f.write(fname[i]+" "+str(randint(1, 10))+"\n")
	for i in xrange(randint(1, 10)):
		if(i%2 == 0):
			f.write("cook "+str(randint(1, 10))+"\n")
		else:
			f.write("prep "+str(randint(1, 10))+"\n")
			
	f.close()