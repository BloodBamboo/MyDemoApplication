//
// Created by 12 on 2020/4/6.
//
#ifndef MYDEMOAPPLICATION_MP3_ENCODER_H
#define MYDEMOAPPLICATION_MP3_ENCODER_H

#endif //MYDEMOAPPLICATION_MP3_ENCODER_H
extern "C" {
#include "lame/lame.h"
}


class Mp3Encoder {
private:
    FILE *pcmFile;
    FILE *mp3File;
    lame_t lameClient;
public:
    Mp3Encoder(){}

    ~Mp3Encoder(){}

    int Init(const char *pcmFilePath, const char *mp3FilePath, int
    sampleRate, int channels, int bitRate);

    void Encode();

    void Destory();
};