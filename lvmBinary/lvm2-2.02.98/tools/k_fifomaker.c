/*
It's make, write and read fifo function
write result, result_pipe,
read cmd, cmd_pipe 
*/

#include "tools.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>

#define MAX_BUFSZ 1024
#define CMDFIFO "cmd_pipe"
#define RESULTFIFO "result_pipe"


void errquit(char *mesg) { perror(mesg); exit(1); }

int  write_fifo() {

	//return result of input command at Java App
	int nbytes, resultwd;
	char *cmd_result;

	//make fifo generate
	if(mkfifo(RESULTFIFO, 0770) == -1 && errno != EEXIST)
		errquit("mkfifo fail");

	resultwd == open(RESULTFIFO, O_WRONLY);
	if(resultwd == -1)
		errquit("fifo open fail");

	//write fifo a result
//	while(1) {
		if(write(resultwd, &cmd_result, sizeof(cmd_result)) < 0)
			errquit("write fail");
		else
		  return 1;
//	}

	return 0;
}

int read_fifo() {
	int nbytes, cmdwd;
	char *cmd_input;

	//make fifo
	/* if(mkfifo(CMDFIFO, 0770) == -1 && errno != EEXIST)
		errquit("cmd mkfifo fail"); */

	cmdwd== open(CMDFIFO, O_RDONLY);
	if(cmdwd == -1)
		errquit("cmdwd open fail";


	while(1) {
	//read fifo a cmd input
		if(read(cmdwd, (char *)&cmd_input, sizeof(cmd_input)) < 0)
			errquit("read cmdwd fail");
		else
		 	return 1;
	}

	return 0;
}



}
