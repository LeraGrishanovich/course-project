package org.example.Utility;


import com.google.gson.Gson;
import org.example.DAO.DAOimpl.FilmDaoImpl;
import org.example.DAO.DAOimpl.SeanceDaoImpl;
import org.example.DAO.DAOimpl.TicketDaoImpl;
import org.example.DAO.DAOimpl.UserDaoImpl;
import org.example.DTO.FilmDto;
import org.example.DTO.SeanceDto;
import org.example.DTO.TicketsDto;
import org.example.DTO.UserDto;
import org.example.Enum.RequestType;
import org.example.Enum.ResponseType;
import org.example.Model.Entity.Film;
import org.example.Model.Entity.Seance;
import org.example.Model.Entity.Ticket;
import org.example.Model.Entity.User;
import org.example.Model.tcp.Request;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientThread implements Runnable {
    //---------------------------------
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    int clientNumber;

    //enums-----------------------------------


    private ResponseType responseType;
    private RequestType requestType;
    //dao-----------------------------------

    private UserDaoImpl userDao = new UserDaoImpl();

    private FilmDaoImpl filmDao = new FilmDaoImpl();

    private SeanceDaoImpl seanceDao = new SeanceDaoImpl();

    private TicketDaoImpl ticketDao = new TicketDaoImpl();

    //dto----------------------------------

    private UserDto userDto = new UserDto();

    private FilmDto filmDto = new FilmDto();

    private SeanceDto seanceDto = new SeanceDto();

    private TicketsDto ticketsDto = new TicketsDto();


    public ClientThread(Socket acceptSocket, int clientNumber) throws IOException {
        this.clientSocket = acceptSocket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(acceptSocket.getOutputStream()));
        this.clientNumber = clientNumber;
    }

    private void sendMessage(String result) throws IOException {
        bufferedWriter.write(result);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private String getMessage() throws IOException {
        String result = bufferedReader.readLine();
        return result;
    }


    @Override
    public void run() {
        String message;
        boolean cycle = true;
        try {
            while (cycle) {
                message = getMessage();
                requestType = new Gson().fromJson(message, Request.class).getRequestType();
                System.out.println("Запрос от клиента № " + clientNumber +": " + requestType);
                switch (requestType) {
                    case LOGIN: {
                        loginCase(message);
                        break;
                    }
                    case REGISTRATION:
                    case ADD_USER: {
                        registrationCase(message);
                        break;
                    }
                    case GET_USERS: {
                        getUsersCase(message);
                        break;
                    }
                    case UPDATE_USER: {
                        updateUserCase(message);
                        break;
                    }
                    case DELETE_USER: {
                        deleteUserCase(message);
                        break;
                    }
                    case GET_FILMS: {
                        getFilmsCase(message);
                        break;
                    }
                    case ADD_FILM: {
                        addFilmCase(message);
                        break;
                    }
                    case UPDATE_FILM: {
                        updateFilmCase(message);
                        break;
                    }
                    case DELETE_FILM: {
                        deleteFilmCase(message);
                        break;
                    }
                    case GET_SEANCES: {
                        getSeancesCase(message);
                        break;
                    }
                    case ADD_SEANCE: {
                        addSeanceCase(message);
                        break;
                    }
                    case UPDATE_SEANCE: {
                        updateSeanceCase(message);
                        break;
                    }
                    case DELETE_SEANCE: {
                        deleteSeanceCase(message);
                        break;
                    }
                    case GET_TICKETS: {
                        getTicketsCase(message);
                        break;
                    }
                    case GET_TICKETS_FOR_ONE_USER: {
                        getTicketsForOneUserCase(message);
                        break;
                    }
                    case ADD_TICKET: {
                        addTicketCase(message);
                        break;
                    }
                    case DELETE_TICKET: {
                        deleteTicketCase(message);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }
        finally {
            System.out.println("Клиент № " + this.clientNumber + " отключился! ");
        }
    }




    private void registrationCase(String message) throws IOException {
        String result;
        User user = userDto.getRequestEntity(message);


        if(userDao.existsByLogin(user)){
            result = new Gson().toJson(ResponseType.FAIL);
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.FAIL);
            sendMessage(result);
            return;
        }

        userDao.saveEntity(user);

        result = new Gson().toJson(ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void loginCase(String message) throws IOException {
        String result;
        User user = userDto.getRequestEntity(message);
        user = userDao.existsByLoginAndPassword(user);
        if(user == null){
            result = userDto.createResponse(new User(), ResponseType.FAIL);
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.FAIL);
            sendMessage(result);
            return;
        }
        else {
            result = userDto.createResponse(user, ResponseType.SUCCESS);
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        }
        sendMessage(result);
    }

    private void getUsersCase(String message) throws IOException {
        List<User> data = userDao.findAllEntity();
        String result;
        result = userDto.createResponse(data, ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void getFilmsCase(String message) throws IOException {
        List<Film> data = filmDao.findAllEntity();
        String result;
        result = filmDto.createResponse(data, ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }
    private void updateUserCase(String message) throws IOException {
        String result;
        User user = userDto.getRequestEntity(message);
        userDao.updateEntity(user);
        result = new Gson().toJson(ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void deleteUserCase(String message) throws IOException {
        try {
            String result;
            User user = userDto.getRequestEntity(message);
            user = userDao.findEntity(user);
            userDao.deleteEntity(user);

            result = new Gson().toJson(ResponseType.SUCCESS);
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
            sendMessage(result);
        }
        catch (Exception e){
            System.out.println("Ошибка удаления пользователя: "+ e.getMessage());
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.FAIL);
        }
    }

    private void deleteFilmCase(String message) throws IOException {
        try {
            String result;
            Film film = filmDto.getRequestEntity(message);
            film = filmDao.findEntity(film);
            filmDao.deleteEntity(film);
            result = new Gson().toJson(ResponseType.SUCCESS);
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
            sendMessage(result);
        }
        catch (Exception e){
            System.out.println("Ошибка удаления фильма: "+ e.getMessage());
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.FAIL);
        }
    }

    private void updateFilmCase(String message) throws IOException {
        String result;
        Film film = filmDto.getRequestEntity(message);
        filmDao.updateEntity(film);
        result = new Gson().toJson(ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void addFilmCase(String message) throws IOException {
        String result;
        Film film = filmDto.getRequestEntity(message);


        filmDao.saveEntity(film);

        result = new Gson().toJson(ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void getSeancesCase(String message) throws IOException {
        List<Seance> data = seanceDao.findAllEntity();
        String result;
        result = seanceDto.createResponse(data, ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void deleteSeanceCase(String message) throws IOException {
        try {
            String result;
            Seance seance = seanceDto.getRequestEntity(message);
            seance = seanceDao.findEntity(seance);
            seanceDao.deleteEntity(seance);

            result = new Gson().toJson(ResponseType.SUCCESS);
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
            sendMessage(result);
        }
        catch (Exception e){
            System.out.println("Ошибка удаления сеанса: "+ e.getMessage());
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.FAIL);
        }
    }

    private void updateSeanceCase(String message) throws IOException {
        String result;
        Seance seance = seanceDto.getRequestEntity(message);
        seanceDao.updateEntity(seance);
        result = new Gson().toJson(ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void addSeanceCase(String message) throws IOException {
        String result;
        Seance seance = seanceDto.getRequestEntity(message);


        seanceDao.saveEntity(seance);

        result = new Gson().toJson(ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void getTicketsCase(String message) throws IOException {
        List<Ticket> data = ticketDao.findAllEntity();
        String result;
        result = ticketsDto.createResponse(data, ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }

    private void deleteTicketCase(String message) throws IOException {
        try {
            String result;
            Ticket ticket = ticketsDto.getRequestEntity(message);

            ticketDao.deleteEntity(ticket);

            result = new Gson().toJson(ResponseType.SUCCESS);
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
            sendMessage(result);
        }
        catch (Exception e){
            System.out.println("Ошибка удаления билета: "+ e.getMessage());
            System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.FAIL);
        }
    }

    private void addTicketCase(String message) throws IOException {
        String result;
        try {
            Ticket ticket = ticketsDto.getRequestEntity(message);
            ticket.setUser(userDao.findEntity(ticket.getUser()));
            ticket.setSeance(seanceDao.findEntity(ticket.getSeance()));
            ticketDao.saveEntity(ticket);

            result = new Gson().toJson(ResponseType.SUCCESS);
            System.out.println("Ответ клиенту № " + clientNumber + ": " + ResponseType.SUCCESS);
            sendMessage(result);
        }
        catch (NullPointerException exception)
        {
            result = new Gson().toJson(ResponseType.FAIL);
            System.out.println("Ответ клиенту № " + clientNumber + ": " + ResponseType.FAIL);
            sendMessage(result);
        }
    }

    private void getTicketsForOneUserCase(String message) throws IOException {
        User user = userDto.getRequestEntity(message);
        List<Ticket> data = ticketDao.findTicketsByUser(user);
        String result;
        result = ticketsDto.createResponse(data, ResponseType.SUCCESS);
        System.out.println("Ответ клиенту № " + clientNumber +": " + ResponseType.SUCCESS);
        sendMessage(result);
    }
}

