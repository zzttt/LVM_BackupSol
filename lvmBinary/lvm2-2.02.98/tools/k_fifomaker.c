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
#include <stdarg.h>


//defined header
#define MAX_BUFSZ 2048
#define CMDFIFO "cmd_pipe"
#define RESULTFIFO "result_pipe"
#define APP_PATH "/data/data/net.kkangsworld.lvmexec/"
#define tmpcmdfifo "/data/data/net.kkangsworld.lvmexec/cmd_pipe"
#define tmpresultfifo "/data/data/net.kkangsworld.lvmexec/result_pipe"

void errquit(char *mesg) { perror(mesg); exit(1); }
void thr_errquit(char* msg, int errcode)
{ printf("%s: %s\n", msg, strerror(errcode));
	pthread_exit(NULL); }

int write_fifo(const char* buffer) {
	//return result of input command at Java App
	int nbytes, resultwd;
	char *cmd_result = "return result of lvm yeah";
	errno = 0;
	 mode_t mode = S_IRWXU | S_IRWXG | S_IRWXO;

	//make fifo generate
	if(mkfifo(tmpresultfifo, 0666) == -1 && errno != EEXIST)
		errquit("result_fifo make fail");

	//resultwd == open(tmpresultfifo, O_WRONLY);
	if((resultwd = open(tmpresultfifo, O_WRONLY)) == -1)
		errquit("result_fifo open fail");

	else {
		printf("[write_fifo][result] : %s\n", buffer);
		printf("[wrtie_fifo][buffsize] : %d\n", strlen(buffer));
		printf("resultwd opened. with FD = %d\n", resultwd);
		
		//write fifo a result
		//while(1) {
		if(nbytes = write(resultwd, buffer, strlen(buffer))) {
			errquit("[result_fifo write fail :]");
			//close(resultwd);
			//unlink(tmpresultfifo);
		}
		else {
			printf("nbytes = %d\n", nbytes);
			//printf("%s\n", strerror(errno));
			perror("[error]:");
			close(resultwd);
			return 1;
		}
	//	}
	}

	return 0;
}

/**
  필요시 parent process kill될 때 같이 reader/writer도 kill하는 거 구현 필요
*/
/* fork fifo function runner */
//int run_fork_fifo(int rw, pipe_msg **pmsg)
//lvm2_main()->run_fork_fifo()->read_fifo()로 연결된다.
int run_fork_fifo(int rw, int pipefd[])
{
	pid_t pid;
	char* buffer[MAX_BUFSZ];
	if(!rw) {
		pid = fork();
		if(pid > 0)
			write_fifo(buffer);
	}
	else if(rw) {
		pid = fork();
		if(pid > 0)
			//read_fifo();
			//test_func(&pmsg);
			read_fifo(pipefd);
	}


}

int test_func() {
		printf("hello world");
}

//function pointer needs
//int run_thread_fifo(int (*func
/*
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
*/

//int read_fifo() {
/* 실질적으로 pipe를 읽는 function run_fork_fifo()로 부터 pmsg addr넘겨 받음 */
//int read_fifo(pipe_msg *pmsg) {
int read_fifo(int pipefd[]) {
	int cmdwd;
	char cmd_input[MAX_BUFSZ];
	pipe_msg *pmsg;
	//*cmd_input = pmsg->cmd;
	//open a cmdfifo usage cmdwd
	cmdwd = open(tmpcmdfifo, O_RDWR);
	if(cmdwd == -1)
		errquit("cmdwd open fail");
	else
		log_print("[wait for command! use PIPE! whiling...]");

	while(1) {
	//read fifo a cmd input
		memset(cmd_input,0, sizeof(cmd_input));
		memset(&pmsg->cmd, 0, sizeof(&pmsg->cmd));
		//if(read(cmdwd, cmd_input, sizeof(cmd_input)) < 0) {
		if(read(cmdwd, &pmsg->cmd, sizeof(&pmsg->cmd)) < 0) {
			errquit("read cmdwd fail");
		}
		
		else {
			//printf("read it : %s\n", cmd_input);
			printf("[read_fifo()]read pmsg : %s\n", pmsg->cmd);
			if(write(pipefd[1], &pmsg->cmd, sizeof(&pmsg->cmd)) < 0)
				log_error("internal pipe writing error");
		//printf("lvm> ");
		}
	}
	close(cmdwd);

	return 1;
}

/* writeresultmsg()
	command를 실행하고 난 result string, value 등을 가지고 pipe에 쓸 수 있도록 준비해 준다.
	값을 write_fifo()에 넘겨서 쓰도록 한다.
 */

int writeresultmsg(const char* format, ...)
{
	char buffer[4096];
	va_list args;
	int len;

	va_start (args, format);
	len = vsprintf(buffer, format, args);
	va_end(args);

	printf("[in writeresultmsg] : %s\n", buffer);
	if(write_fifo(&buffer))
		return 1;
	else
		return 0;
}


	
