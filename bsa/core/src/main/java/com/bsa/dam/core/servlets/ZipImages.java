/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.bsa.dam.core.servlets;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import org.apache.felix.scr.annotations.Property;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.*;
import java.net.URL;
import java.awt.image.*;
import java.io.InputStream;


/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@SuppressWarnings("serial")
@SlingServlet(paths="/bin/bsa/zip", methods = "GET", metatype=true)
@Property(name = "zip", value = "zip")
public class ZipImages extends org.apache.sling.api.servlets.SlingAllMethodsServlet {

    @Override
    protected void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException, IOException {
        // Set the content type based to zip
        /*response.setContentType("Content-type: text/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=mytest.zip");*/

        BufferedImage image = null;
        try {

            URL url = new URL("http://localhost:4502/content/dam/trail-blazer/Britton,Chris.JPG");
            image = ImageIO.read(url);
            ImageIO.write(image,"jpg",new File("C:\\Users\\Aditya\\Downloads\\AEM_6.2\\text.jpg"));
            // get DataBufferBytes from Raster

            /*ServletOutputStream out = response.getOutputStream();
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));
            zos.putNextEntry(new ZipEntry(url.getFile()));

            zos.closeEntry();
            zos.close();*/
        /*InputStream    is = url.openStream();
            zos.putNextEntry(new ZipEntry(url.getFile()));
            int length;

            byte[] b = new byte[4096];

            while((length = is.read(b)) > 0) {
                zos.write(b, 0, length);
            }
            zos.closeEntry();
            is.close();*/
           // zos.putNextEntry(new ZipEntry("Steve.jpg"));
            //while ((data = image.read()) != -1) {
             //   zos.write(data.getData());
           // }

           // zos.closeEntry();
           // zos.close();
           // List<File> files = new ArrayList<File>();
            //files.add(image);
            //BufferedInputStream fif = new BufferedInputStream(fis);




        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");

        /*// List of files to be downloaded
        List<File> files = new ArrayList<File>();
        files.add(new File("/content/dam/trail-blazer/Britton,Chris.JPG"));
        *//*files.add(new File("C:/second.txt"));
        files.add(new File("C:/third.txt"));*//*
        ServletOutputStream out = response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));

        for (File file : files) {

            System.out.println("Adding " + file.getName());
            zos.putNextEntry(new ZipEntry(file.getName()));

            // Get the file
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);

            } catch (FileNotFoundException fnfe) {
                // If the file does not exists, write an error entry instead of
                // file
                // contents
                zos.write(("ERRORld not find file " + file.getName())
                        .getBytes());
                zos.closeEntry();
                System.out.println("Couldfind file "
                        + file.getAbsolutePath());
                continue;
            }

            BufferedInputStream fif = new BufferedInputStream(fis);

            // Write the contents of the file
            int data = 0;
            while ((data = fif.read()) != -1) {
                zos.write(data);
            }
            fif.close();

            zos.closeEntry();
            System.out.println("Finishedng file " + file.getName());
        }

        zos.close();*/

    }
}
