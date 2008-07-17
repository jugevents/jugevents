// Copyright 2006-2008 The Parancoe Team
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package it.jugpadova.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A controller for managing jcaptcha verification.
 *
 * @author lucio
 */
@Controller
@RequestMapping("/jcaptcha/*.html")
public class JCaptchaController {

    private static final Logger logger = Logger.getLogger(
            JCaptchaController.class);
    @Autowired
    private ImageCaptchaService captchaService;

    @RequestMapping
    public void image(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

        // get the session id that will identify the generated captcha.
        //the same id must be used to validate the res, the session id is a good candidate!
        String captchaId = req.getSession().getId();

        // call the ImageCaptchaService getChallenge method
        BufferedImage challenge = captchaService.getImageChallengeForID(
                captchaId, req.getLocale());

        // a jpeg encoder
        JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(
                jpegOutputStream);
        jpegEncoder.encode(challenge);

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // flush it in the res
        res.setHeader("Cache-Control", "no-store");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setContentType("image/jpeg");
        ServletOutputStream resOutputStream = res.getOutputStream();
        resOutputStream.write(captchaChallengeAsJpeg);
        resOutputStream.flush();
        resOutputStream.close();
    }
}
