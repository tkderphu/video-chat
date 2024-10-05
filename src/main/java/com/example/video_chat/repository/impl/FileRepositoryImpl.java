//package com.example.video_chat.repository.impl;
//
//import com.example.video_chat.model.FileEntity;
//import com.example.video_chat.model.FileType;
//import com.example.video_chat.repository.FileRepository;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.sql.*;
//import java.util.List;
//
//@Repository
//public class FileRepositoryImpl implements FileRepository {
//    private JdbcTemplate jdbcTemplate;
//    @Override
//    public FileEntity save(MultipartFile file, FileType fileType) {
//
//    }
//
//    @Override
//    public void delete(String url) {
//        String sql = "DELETE FROM file WHERE url = ?";
//        jdbcTemplate.update(sql, url);
//    }
//
//    @Override
//    public FileEntity findByUrl(String url) {
//        String sql = "SELECT * FROM file WHERE url = ?";
//        String db_url = "jdbc:mysql://localhost:3306/video_chat";
//        String db_user = "project";
//        String db_password = "project";
//        FileEntity fileEntity = new FileEntity();
//        try(Connection conn = DriverManager.getConnection(db_url, db_user, db_password);
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            ){
//            while(rs.next()) {
//                fileEntity.setId(rs.getInt("id"));
//                fileEntity.setName(rs.getString("name"));
//                fileEntity.setUrl(rs.getString("url"));
//            }
//
//
//        }catch(SQLException e){
//            e.printStackTrace();
//            System.out.println("File not exist");
//        }
//        return fileEntity;
//    }
//
//
//}
