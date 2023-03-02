package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.UsuariosAdapter;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvUser;
    private final TextView name;
    private final ConstraintLayout constraintLayout;


    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        tvUser = itemView.findViewById(R.id.user_login_list);
        name = itemView.findViewById(R.id.user_name_list);
        constraintLayout = itemView.findViewById(R.id.container_usuarios_server);
    }


    public void bind(final Usuario user, final UsuariosAdapter.OnItemClickListener listener, Context context){

        if (user != null){

            String nombre = user.getNombre() + user.getApellido_p() + user.getApellido_m();
            tvUser.setText(user.getUser());
            name.setText(nombre);


            SharedPreferences prefs = context.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);

            if (prefs.getString(Utilidades.SHARED_SERVER_ID_USER, "").equals(String.valueOf(user.getId_usuario()))){
                constraintLayout.setBackgroundColor(context.getColor(R.color.colorSelected));
                tvUser.setTextColor(context.getColor(R.color.colorOnSurface));
            }else{
                constraintLayout.setBackgroundColor(context.getColor(R.color.colorSurface));
                tvUser.setTextColor(context.getColor(R.color.colorOnSurface));
            }

            itemView.setOnClickListener(v -> listener.onItemClick(user));
        }


    }

}
