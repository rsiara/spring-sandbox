package com.springstart.Controller.Util;


import com.springstart.Utils.StreamSandbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/utils")
public class StreamSandboxController {

    private final Logger logger = LoggerFactory.getLogger(StreamSandboxController.class);

    private StreamSandbox streamSandbox;

    @RequestMapping(value = "/streamdo", method = RequestMethod.GET)
    public void doSomethingUsingStream(Model model) {
        streamSandbox.doSomething();
    }

    public StreamSandbox getStreamSandbox() {
        return streamSandbox;
    }

    @Autowired
    public void setStreamSandbox(StreamSandbox streamSandbox) {
        this.streamSandbox = streamSandbox;
    }
}
