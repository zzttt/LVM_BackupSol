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
#include <pthread.h>


//defined header
#define MAX_BUFSZ 1024
#define CMDFIFO "cmd_pipe"
#define RESULTFIFO "result_pipe"
#define APP_PATH "/data/data/net.kkangsworld.lvmexec/"
#define tmpcmdfifo "/data/data/net.kkangsworld.lvmexec/cmd_pipe"
#define tmpresultfifo "/data/data/net.kkangsworld.lvmexec/result_pipe"

void errquit(char *mesg) { perror(mesg); exit(1); }
void thr_errquit(char* msg, int errcode)
{ printf("%s: %s\n", msg, strerror(errcode));
	pthread_exit(NULL); }

int  write_fifo() {

	//return result of input command at Java App
	int nbytes, resultwd;
	char *cmd_result = "return result of lvm yeah";

	//make fifo generate
	if(mkfifo(tmpresultfifo, 0770) == -1 && errno != EEXIST)
		errquit("result_fifo make fail");

	resultwd == open(tmpresultfifo, O_WRONLY);
	if(resultwd == -1)
		errquit("result_fifo open fail");

	//write fifo a result
//	while(1) {
		if(write(resultwd, &cmd_result, sizeof(cmd_result)) < 0)
			errquit("result_fifo write fail");
		else
		  return 1;
//	}

	return 0;
}


/* fork fifo function runner */
int run_fork_fifo(int rw)
{
	pid_t pid;
	
	if(!rw) {
		pid = fork();
		if(pid > 0)
			write_fifo();
	}
	else if(rw) {
		pid = fork();
		if(pid > 0)
			read_fifo();
	}


}


//function pointer needs
//int run_thread_fifo(int (*func
int run_thread_fifo(int rw)
{
	pthread_t tid[10];
	int status;

	if(rw==1)
	{
		if((status=pthread_create(&tid[1], NULL, read_fifo, NULL))!=0)
			thr_errquit("ERR! pthread_create with read_fifo\n", errno);
		pthread_join(&tid[1], NULL);
		log_print("Use pthread read_fifo\n");
		return 1;
	}

	else if(rw==0)
	{
		if((status=pthread_create(&tid[0], NULL, write_fifo, NULL))!=0)
			thr_errquit("ERR! pthread_create with write_fifo\n", errno);
		pthread_join(&tid[0], NULL);
		log_print("Use pthread write_fifo\n");
		return 0;
	}

	return -1;
}

//int read_fifo(char* cmd_aaa) {
int read_fifo() {
	int nbytes, cmdwd;
	char *cmd_input;
	int status;

	//make fifo
	/* if(mkfifo(CMDFIFO, 0770) == -1 && errno != EEXIST)
		errquit("cmd mkfifo fail"); */

	//thread run
	//pthread_t tid[10];

	//create thread
	/*if((status=pthread_create(&tid[0], NULL, NULL))!=0)
		thr_errquit("pthread_create", status);
	*/
	cmdwd = open(tmpcmdfifo, O_RDONLY);
	if(cmdwd == -1)
		errquit("cmdwd open fail");
	else
		log_print("wait for command! whiling...");

	while(1) {
	//read fifo a cmd input
		if(read(cmdwd, (char *)&cmd_input, sizeof(cmd_input)) < 0)
			errquit("read cmdwd fail");
		else {
			printf("read it : %s\n", cmd_input);
		 	//return 1;
		}
	}

	return 0;
}


