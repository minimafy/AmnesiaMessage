package com.example.prueba.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Adaptadores.UserAdapter;
import com.example.prueba.Adaptadores.UserChatDisplayAdapter;
import com.example.prueba.ChatRoom;
import com.example.prueba.Objetos.Chat;
import com.example.prueba.Objetos.TareaBorrarMensaje;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsDisplayFragment extends Fragment {
    //Declaramos los atributos
    private RecyclerView recyclerView;
    private TextView sinChats;
    private TextView cargandoChats;
    private UserChatDisplayAdapter userChatDisplayAdapter;
    private ArrayList<Usuario2> usuarioList;
    private ArrayList<String> id_list;
    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private DatabaseReference referencia2;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DividerItemDecoration divider;
    ValueEventListener listener;


    public ChatsDisplayFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chatdisplay, container, false);
    //Creamos el timer que ejecutará la tarea de borrar mensajes , la cual borrará los mensajes con más de 24H . Esté lanzará la tarea cada 5 minutos
        Timer timer=new Timer();
        TareaBorrarMensaje tarea=new TareaBorrarMensaje();
        timer.scheduleAtFixedRate(tarea,0,300000);
        //Enlazamos las vistas y los atributos
        sinChats = view.findViewById(R.id.chat_display_sinchats);
        cargandoChats = view.findViewById(R.id.chat_display_cargando);
        base=FirebaseDatabase.getInstance();
        //Obtenemos los elementos de Firebase necesarios
        referencia=base.getReference("Chats");
        referencia2=base.getReference("Usuarios");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        id_list=new ArrayList<>();
        usuarioList=new ArrayList<>();
        //Creamos el recycler view y le agregamos el divider decorativo
        recyclerView = view.findViewById(R.id.recycler_view_chat_display);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        userChatDisplayAdapter = new UserChatDisplayAdapter(getContext(),usuarioList);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.reycler_divider));
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(userChatDisplayAdapter);
        //Leemos los datos
        leerChats();





        return view;

    }
    //Este método se encarga en primer lugar de leer el último mensaje de cada usuario con los que hemos intercambiado mensajes
    //ordenados de forma cronológica, después lo invertimos mediante un for para que estén de forma cronologicamente inversa y por último
    //Obtenemos los usuarios de dichos chats para mostrarlos en la pantalla
    public void leerChats(){
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id_list.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Chat chat= data.getValue(Chat.class);
                    Boolean existe_ya=false;
                    String id;
                    int posicion=0;
                    if(chat.getReceiver().equals(user.getUid())){
                        id=chat.getSender();
                        for(int i=0;i<id_list.size();i++){
                            if(id_list.get(i).equals(id)){
                                existe_ya=true;
                                posicion=i;
                            }
                        }
                        if(existe_ya==false){
                            id_list.add(id);
                        }
                        else{
                            id_list.remove(posicion);
                            id_list.add(id);
                        }
                    }
                    else if(chat.getSender().equals(user.getUid())){
                        id=chat.getReceiver();
                        for(int i=0;i<id_list.size();i++){
                            if(id_list.get(i).equals(id)){
                                existe_ya=true;
                                posicion=i;
                            }
                        }
                        if(existe_ya==false){
                            id_list.add(id);
                        }else{
                            id_list.remove(posicion);
                            id_list.add(id);
                        }
                    }
                }
                referencia2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> id_list2=new ArrayList<>();
                        for(int i=id_list.size()-1;i>-1;i--){
                            id_list2.add(id_list.get(i));
                        }
                        usuarioList.clear();
                        ArrayList<Usuario> usuarios=new ArrayList<>();
                        Usuario usuario=new Usuario();
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                           usuarios.add(usuario=data.getValue(Usuario.class));
                        }

                        for(int i=0;i<id_list2.size();i++){
                            for(int j=0;j<usuarios.size();j++){
                                if(id_list2.get(i).equals(usuarios.get(j).getId())){
                                    usuarioList.add(new Usuario2(dataSnapshot.getKey(),usuarios.get(j).getId(),usuarios.get(j).getNombre_usuario(),
                                            usuarios.get(j).getEmail(),usuarios.get(j).getTelefono(),usuarios.get(j).getUrl_imagen()));
                                }
                            }
                        }
                        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
                        userChatDisplayAdapter = new UserChatDisplayAdapter(getContext(),usuarioList);
                        userChatDisplayAdapter.setOnClickListener(new UserChatDisplayAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position) {
                                Intent intent = new Intent(getContext(), ChatRoom.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("usuario",usuarioList.get(position));
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                        cargandoChats.setVisibility(View.GONE);
                        if(usuarioList.size()==0){
                            sinChats.setVisibility(View.VISIBLE);
                        }
                        else{
                            sinChats.setVisibility(View.INVISIBLE);
                        }
                        recyclerView.setAdapter(userChatDisplayAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






}
