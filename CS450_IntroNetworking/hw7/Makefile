TARGETS=hw7_sender hw7_receiver

all: $(TARGETS)

hw7_sender: hw7_sender.c hw7.c
	gcc --std=gnu99 -o hw7_sender hw7_sender.c hw7.c

hw7_receiver: hw7_receiver.c hw7.c
	gcc --std=gnu99 -o hw7_receiver hw7_receiver.c hw7.c
 
clean:
	rm -f $(TARGETS)
