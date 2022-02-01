package cl.smapdev.curimapu.clases.bd;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migrations {

//    public static final Migration MIGRATION_1_TO_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//
//            database.execSQL("ALTER TABLE visita ADD COLUMN  id_visita_local INTEGER DEFAULT 0 NOT NULL");
//            database.execSQL("ALTER TABLE temp_visitas ADD COLUMN  id_visita_local INTEGER DEFAULT 0 NOT NULL");
//            database.execSQL("ALTER TABLE visita ADD COLUMN  id_dispo INTEGER DEFAULT 0 NOT NULL");
//            database.execSQL("ALTER TABLE temp_visitas ADD COLUMN  id_dispo INTEGER DEFAULT 0 NOT NULL");
//            database.execSQL("ALTER TABLE fotos ADD COLUMN  id_visita_servidor_foto INTEGER DEFAULT 0 NOT NULL");
//            database.execSQL("ALTER TABLE fotos ADD COLUMN  id_dispo_foto INTEGER DEFAULT 0 NOT NULL");
//        }
//    };


    public static final Migration MIGRATION_1_TO_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE fotos_fichas ( " +
                    " id_fotos_fichas INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    " nombre_foto_ficha TEXT , " +
                    " ruta_foto_ficha TEXT , " +
                    " id_ficha_fotos_local INTEGER NOT NULL DEFAULT 0, " +
                    " id_ficha_fotos_servidor INTEGER NOT NULL DEFAULT 0, " +
                    " fecha_hora_captura TEXT  DEFAULT '0000-00-00 00:00:00', " +
                    " id_usuario_captura INTEGER NOT NULL DEFAULT 0, " +
                    " id_dispo_captura INTEGER NOT NULL DEFAULT 0, " +
                    " estado_subida_foto INTEGER NOT NULL DEFAULT 0, " +
                    " encrypted_image TEXT " +
                    ");");

            database.execSQL("ALTER TABLE ficha ADD COLUMN id_ficha_local_ficha INTEGER DEFAULT 0 NOT NULL");
        }
    };

    public static final Migration MIGRATION_2_TO_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE fotos_fichas ADD COLUMN cabecera_subida INTEGER NOT NULL DEFAULT 0 ");
        }
    };


    public static final Migration MIGRATION_3_TO_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE agri_pred_temp ( " +
                    "id_agric INTEGER NOT NULL PRIMARY KEY ," +
                    "id_pred INTEGER NOT NULL DEFAULT 0, " +
                    "id_tempo INTEGER NOT NULL DEFAULT 0, " +
                    "norting TEXT  , " +
                    "easting TEXT " +
                    "); ");
//            database.execSQL("ALTER TABLE predio DROP id_agric, DROP id_tempo ");
        }
    };

    public static final Migration MIGRATION_4_TO_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE ficha ADD COLUMN id_provincia_ficha TEXT ; ");
        }
    };

    public static final Migration MIGRATION_5_TO_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE materiales ADD condition TEXT ; ");
            database.execSQL("ALTER TABLE materiales ADD certification TEXT ; ");

            database.execSQL("ALTER TABLE anexo_contrato ADD ready_batch TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sectorial_office TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD field_sag TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD rch TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sag_register_number TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD has_contrato TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD has_gps TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD orden_multiplicacion TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sowing_date TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD lines_female TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD lines_male TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sl_female TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sl_real_sowing_female TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sl_male TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sl_real_sowing_male TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sl_line_increase TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD sl_real_sowing_increase_line TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD results_raw_kgs TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD results_clean_kgs TEXT ; ");
            database.execSQL("ALTER TABLE anexo_contrato ADD yield_kg_ha TEXT ; ");




        }
    };

    public static final Migration MIGRATION_6_TO_7 = new Migration(6,7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE anexo_contrato ADD linea_incremento TEXT ; ");
        }
    };

    public static final Migration MIGRATION_7_TO_8 = new Migration(7,8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE materiales ADD condition_mat TEXT ; ");

        }
    };

    public static final Migration MIGRATION_8_TO_9 = new Migration(8,9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE crop_rotation ADD tipo_crop TEXT ; ");

        }
    };


    public static final Migration MIGRATION_9_TO_10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE ficha_new ( " +
                    " id_ficha_new INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    " id_ficha_local_ficha_new INTEGER NOT NULL DEFAULT 0, " +
                    " id_tempo_new TEXT , " +
                    " id_agric_new TEXT, " +
                    " oferta_de_negocio_new TEXT, " +
                    " id_region_new TEXT, " +
                    " id_comuna_new TEXT, " +
                    " id_provincia_new TEXT, " +
                    " localidad_new TEXT, " +
                    " ha_disponibles_new REAL NOT NULL  DEFAULT 0.0,  " +
                    " obs_new TEXT,  " +
                    " norting_new REAL NOT NULL  DEFAULT 0.0,  " +
                    " easting_new REAL NOT NULL DEFAULT 0.0,  " +
                    " id_est_fic_new INTEGER NOT NULL DEFAULT 0,  " +
                    " subida_new INTEGER NOT NULL DEFAULT 0,  " +
                    " id_pred_new INTEGER NOT NULL DEFAULT 0,  " +
                    " id_lote_new INTEGER NOT NULL DEFAULT 0 ,  " +
                    " coo_utm_ref_new TEXT ,  " +
                    " coo_utm_ampros_new TEXT ,  " +
                    " id_tipo_suelo_new TEXT ,  " +
                    " id_tipo_riego_new TEXT ,  " +
                    " experiencia_new TEXT ,  " +
                    " id_usuario_new INTEGER NOT NULL DEFAULT 0 ,  " +
                    " id_tipo_tenencia_maquinaria_new TEXT ,  " +
                    " id_tipo_tenencia_terreno_new TEXT ,  " +
                    " maleza_new TEXT ,  " +
                    " cabecera_new INTEGER NOT NULL DEFAULT 0 ,  " +
                    " predio_new TEXT ,  " +
                    " potrero_new TEXT ,  " +
                    " especie_new TEXT ,  " +
                    " estado_general_new TEXT ,  " +
                    " fecha_limite_siembra_new TEXT ,  " +
                    " observacion_negocio_new_new TEXT " +
                    ");");

        }
    };

    public static final Migration MIGRATION_10_TO_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE anexo_correo_fechas ( " +
                    " id_ac_cor_fech INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    " id_ac_corr_fech INTEGER NOT NULL DEFAULT 0, " +
                    " id_fieldman INTEGER NOT NULL DEFAULT 0, " +
                    " inicio_despano TEXT , " +
                    " correo_inicio_despano INTEGER NOT NULL DEFAULT 0, " +
                    " cinco_porciento_floracion TEXT , " +
                    " correo_cinco_porciento_floracion INTEGER NOT NULL DEFAULT 0, " +
                    " inicio_corte_seda TEXT , " +
                    " correo_inicio_corte_seda INTEGER NOT NULL DEFAULT 0, " +
                    " inicio_cosecha TEXT , " +
                    " correo_inicio_cosecha INTEGER NOT NULL DEFAULT 0, " +
                    " termino_cosecha TEXT , " +
                    " correo_termino_cosecha INTEGER NOT NULL DEFAULT 0, " +
                    " termino_labores_post_cosechas TEXT , " +
                    " correo_termino_labores_post_cosechas INTEGER NOT NULL DEFAULT 0, " +
                    " detalle_labores TEXT , " +
                    " id_asistente INTEGER NOT NULL DEFAULT 0 );");

        }
    };



    public static final Migration MIGRATION_11_TO_12 = new Migration(11,12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE temporada ADD especial_temporada INTEGER NOT NULL DEFAULT 0 ; ");

        }
    };


    public static final Migration MIGRATION_12_TO_13 = new Migration(12,13) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE visita ADD tomadas INTEGER NOT NULL DEFAULT 0 ; ");
            database.execSQL("ALTER TABLE detalle_visita_prop ADD tomada_detalle INTEGER NOT NULL DEFAULT 0 ; ");
            database.execSQL("ALTER TABLE fotos ADD tomada_foto INTEGER NOT NULL DEFAULT 0 ; ");

        }
    };


    public static final Migration MIGRATION_13_TO_14 = new Migration(13,14) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE pro_cli_mat ADD COLUMN id_sub_propiedad_pcm INTEGER NOT NULL DEFAULT 0 ; ");
        }
    };
}
