#ifndef _K_FIFOMAKER_H
#define _K_FIFOMAKER_H
#endif

#ifdef __cplusplus
extern "C" {
#endif

//#ifndef _WRITE_FIFO
void errquit(char* dmesg);
int write_fifo();
int read_fifo();
#ifdef __cplusplus
}
#endif

