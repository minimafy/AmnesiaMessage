package com.example.prueba.Objetos;

import java.util.Date;

public class Chat {
    //Declaramos los atributos de la clase
    private String sender;
    private String receiver;
    private String message;
    private String type;
    private Date fecha;
    private String url_imagen;
    private boolean notificado;

    private boolean leido;
    //Metodo constructor
    public Chat(String sender, String receiver, String message, Date fecha,String type,String url_imagen) {
        this.leido=false;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.fecha = fecha;
        this.url_imagen=url_imagen;
        this.type = type;
    }
    //Metodo constructor vacio
    public Chat() {

    }
    //Getters y setters de los atributos
    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public boolean isNotificado() {
        return notificado;
    }

    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
    }
}
