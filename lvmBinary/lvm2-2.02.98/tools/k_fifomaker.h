#ifndef _K_FIFOMAKER_H
#define _K_FIFOMAKER_H
#endif

#define MAX_BUFSZ 2048
#define CMDFIFO "cmd_pipe"
#define RESULTFIFO "result_pipe"

/* struct defined */

typedef struct pipe_message {
	int types; //write == 0, read ==1
	char cmd[MAX_BUFSZ]; //command msg
} pipe_msg;




#ifdef __cplusplus
extern "C" {
#endif

//#ifndef _WRITE_FIFO
void errquit(char* dmesg);
void thr_errquit(char *msg, int errcode);
int run_thread_fifo(int rw);
//int run_fork_fifo(int rw, pipe_msg **pmsg);
int run_fork_fifo(int rw, int pipefd[]);
int write_fifo();
//int read_fifo(pipe_msg *pmsg);
//int run_fork_fifo(int rw);
int read_fifo(int pipefd[]);

#ifdef __cplusplus
}
#endif

