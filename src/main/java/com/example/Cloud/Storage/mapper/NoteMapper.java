package com.example.Cloud.Storage.mapper;

import com.example.Cloud.Storage.model.NoteModel;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface NoteMapper {

    /**
     * @Param userId
     * @Returns ArrayList of type NoteModel of given user
     */
    @Select("SELECT NOTES.noteid, NOTES.notetitle, NOTES.notedescription, NOTES.userid \n" +
            "FROM NOTES \n" +
            "INNER JOIN USERS ON NOTES.userid = USERS.userid \n" +
            "WHERE USERS.username = #{username};\n")
    ArrayList<NoteModel> getAllNotes(String username);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    NoteModel getNote(Integer noteId);

    @Select("SELECT userid FROM NOTES WHERE noteid = #{noteId}")
    int getUserId(Integer noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int createNote(NoteModel noteModel);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    void updateNote(NoteModel noteModel);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    int deleteNote(Integer noteId);

}
