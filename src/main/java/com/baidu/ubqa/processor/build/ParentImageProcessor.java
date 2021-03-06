package com.baidu.ubqa.processor.build;

import com.baidu.ubqa.client.DockerRemoteHandler;
import com.baidu.ubqa.config.RuntimeConfiguration;
import com.baidu.ubqa.entity.Image;
import com.baidu.ubqa.entity.ProcessorResult;
import com.baidu.ubqa.entity.Registry;
import com.baidu.ubqa.entity.Result;
import com.baidu.ubqa.processor.Processor;
import com.spotify.docker.client.DockerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ParentImageProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(ParentImageProcessor.class);

    @Override
    public ProcessorResult proceess(Image image, RuntimeConfiguration configuration) {
        DockerRemoteHandler dockerRemoteHandler = new DockerRemoteHandler();
        Registry registry = image.getBaseImage().getRegistry();
        final DockerClient dockerClient = dockerRemoteHandler.buildDockerClient(registry, configuration.getDockerHost());
        Result result = dockerRemoteHandler.pull(dockerClient, image.getBaseImage());
        if(result.getStatus()){
            return new ProcessorResult(image,configuration).success();

        }else{
            return new ProcessorResult(image,configuration).error(result.getInfo());
        }
    }
}
