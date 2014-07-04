#ifndef _K_FIFOMAKER_H
#define _K_FIFOMAKER_H
#endif

#define MAX_BUFSZ 1024
#define CMDFIFO "cmd_pipe"
#define RESULTFIFO "result_pipe"

/* struct defined */
/*
struct typedef pipe_msg {
int types;
char* cmd;
}
*/


#ifdef __cplusplus
extern "C" {
#endif

//#ifndef _WRITE_FIFO
void errquit(char* dmesg);
void thr_errquit(char *msg, int errcode);
int run_thread_fifo(int rw);
int write_fifo();
int read_fifo();
#ifdef __cplusplus
}
#endif

