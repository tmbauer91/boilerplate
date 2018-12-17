package com.boilerplate.missionDatabase.mission;

import org.springframework.data.annotation.Id;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

@Document(collection = "mission")
public class Mission {

    @Id
    public String id;
    private String name;
    private MissionType type;
    private Binary fileData;

    public Mission(){}

    public Mission(MultipartFile fileData) throws  IOException{

        String name = fileData.getOriginalFilename();
        if(name.contains(".miz")){
            this.type = MissionType.DCS;
        } else if (name.contains(".pbo")) {
            this.type = MissionType.ARMA;
        } else {
            throw new IOException("Wrong File Type");
        }

        //this.name = name.substring(0, name.indexOf('.'));
        this.name = name;
        this.fileData = new Binary(fileData.getBytes());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MissionType getType() {
        return type;
    }

    public void setType(MissionType type) {
        this.type = type;
    }

    public Binary getFileData() {
        return fileData;
    }

    public void setFileData(Binary fileData) {
        this.fileData = fileData;
    }

    public File getFile() throws IOException {
        return binaryToFile(fileData);
    }

    public void addBriefingFile(MultipartFile[] files) throws IOException {
        if(this.type != MissionType.DCS) {
            throw new IOException("Must be DCS.miz file");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipInputStream zin = new ZipInputStream(new FileInputStream(binaryToFile(this.fileData)));
        ZipOutputStream zout = new ZipOutputStream(bos);

        byte[] buf = new byte[1024];

        ZipEntry entry = zin.getNextEntry();
        boolean kneeboardExists = false;
        while(entry != null){
            if(entry.getName().equals("KNEEBOARD")){
                kneeboardExists = true;
            } else {
                zout.putNextEntry(new ZipEntry(entry.getName()));
                int len;
                while ((len = zin.read(buf)) > 0) {
                    zout.write(buf, 0 , len);
                }
            }
            entry = zin.getNextEntry();
        }
        zin.close();
        if(!kneeboardExists){
            zout.putNextEntry( new ZipEntry("KNEEBOARD"));
        }

        for(MultipartFile file : files){
            InputStream in = file.getInputStream();
            String path = "KNEEBOARD/" + file.getName();
            zout.putNextEntry(new ZipEntry(path));
            int len;
            while ((len = in.read(buf)) > 0) {
                zout.write(buf, 0 , len);
            }
            zout.closeEntry();
            in.close();
        }
        zout.close();
        this.fileData = new Binary(bos.toByteArray());
    }


    private File binaryToFile(Binary binary) throws IOException {

        File tempFile = File.createTempFile(name,type.getExtension(),null);
        try(FileOutputStream stream = new FileOutputStream(tempFile)){
            stream.write(binary.getData());
        }
        return tempFile;
    }

    @Override
    public String toString() {
        return String.format("File[id=%s, name=%s, type=%s']",
                id, name, type.getGame());
    }


}
