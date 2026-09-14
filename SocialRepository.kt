package com.example.data.repository

import com.example.data.dao.SocialDao
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.CommentEntity
import com.example.data.entity.EventEntity
import com.example.data.entity.GroupEntity
import com.example.data.entity.PostEntity
import com.example.data.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

class SocialRepository(private val socialDao: SocialDao) {

    val allPosts: Flow<List<PostEntity>> = socialDao.getAllPosts()
    val allEvents: Flow<List<EventEntity>> = socialDao.getAllEvents()
    val allGroups: Flow<List<GroupEntity>> = socialDao.getAllGroups()
    val userProfile: Flow<UserProfileEntity?> = socialDao.getUserProfile()
    val allMessages: Flow<List<ChatMessageEntity>> = socialDao.getAllMessages()

    fun getCommentsForPost(postId: Int): Flow<List<CommentEntity>> {
        return socialDao.getCommentsForPost(postId)
    }

    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessageEntity>> {
        return socialDao.getMessagesForConversation(conversationId)
    }

    suspend fun createPost(
        authorName: String,
        authorType: String,
        authorAges: String,
        location: String,
        avatarColor: Long,
        avatarInitials: String,
        content: String,
        lifestyleTag: String,
        imageResName: String = "",
        isPrivateVault: Boolean = false
    ): Long {
        val post = PostEntity(
            authorName = authorName,
            authorType = authorType,
            authorAges = authorAges,
            location = location,
            avatarColor = avatarColor,
            avatarInitials = avatarInitials,
            content = content,
            lifestyleTag = lifestyleTag,
            imageResName = imageResName,
            likesCount = 1,
            commentsCount = 0,
            userReaction = "LIKE",
            isPrivateVault = isPrivateVault,
            timestamp = System.currentTimeMillis()
        )
        return socialDao.insertPost(post)
    }

    suspend fun reactToPost(postId: Int, newReaction: String, currentReaction: String) {
        val likesDelta = when {
            currentReaction.isEmpty() && newReaction.isNotEmpty() -> 1
            currentReaction.isNotEmpty() && newReaction.isEmpty() -> -1
            else -> 0
        }
        socialDao.updatePostReaction(postId, newReaction, likesDelta)
    }

    suspend fun addComment(
        postId: Int,
        authorName: String,
        authorType: String,
        avatarInitials: String,
        avatarColor: Long,
        text: String
    ) {
        val comment = CommentEntity(
            postId = postId,
            authorName = authorName,
            authorType = authorType,
            avatarInitials = avatarInitials,
            avatarColor = avatarColor,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        socialDao.insertComment(comment)
        socialDao.incrementCommentsCount(postId)
    }

    suspend fun sendMessage(
        conversationId: String,
        senderName: String,
        text: String,
        isMine: Boolean = true
    ) {
        val msg = ChatMessageEntity(
            conversationId = conversationId,
            senderName = senderName,
            text = text,
            timestamp = System.currentTimeMillis(),
            isMine = isMine
        )
        socialDao.insertChatMessage(msg)
    }

    suspend fun toggleEventAttendance(eventId: Int, currentlyAttending: Boolean) {
        val newAttending = !currentlyAttending
        val delta = if (newAttending) 1 else -1
        socialDao.updateEventAttendance(eventId, newAttending, delta)
    }

    suspend fun toggleGroupMembership(groupId: Int, currentlyJoined: Boolean) {
        val newJoined = !currentlyJoined
        val delta = if (newJoined) 1 else -1
        socialDao.updateGroupMembership(groupId, newJoined, delta)
    }

    suspend fun updateDiscreetMode(isDiscreet: Boolean) {
        socialDao.updateDiscreetMode(isDiscreet)
    }

    suspend fun updateProfile(profile: UserProfileEntity) {
        socialDao.insertUserProfile(profile)
    }

    suspend fun checkAndSeedInitialData() {
        if (socialDao.getPostCount() > 0) return

        // Seed User Profile
        socialDao.insertUserProfile(
            UserProfileEntity(
                id = 1,
                profileName = "Alex & Sofía",
                profileType = "Pareja Hetero-curiosa",
                ages = "Él 33, Ella 30",
                city = "Madrid, España",
                bio = "Amantes del buen vino, cenas con calma y gente auténtica. Buscamos compartir momentos distendidos con parejas educadas y respetuosas. La química y el buen rollo son lo primero.",
                seeking = "Parejas afines para salir de copas, charlar sin tabúes y lo que fluya con naturalidad.",
                boundaries = "Respeto total, comunicación transparente, sin presiones y discreción mutua.",
                isVerified = true,
                isDiscreetMode = false
            )
        )

        // Seed Posts
        val initialPosts = listOf(
            PostEntity(
                authorName = "Carlos & Valeria",
                authorType = "Pareja Swinger",
                authorAges = "34 & 31",
                location = "Madrid • Salamanca",
                avatarColor = 0xFFE11D62,
                avatarInitials = "CV",
                content = "¡Hola a la comunidad de Swingers FB! Planeando una escapada de fin de semana para cenar en terraza y tomar algo con otra pareja afín. Nos encantan las conversaciones amenas y sin prisas. ¿Alguien por la zona con ganas de compartir unas copas?",
                lifestyleTag = "Cena & Copas",
                imageResName = "img_lifestyle_banner",
                likesCount = 38,
                commentsCount = 7,
                userReaction = "",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 45
            ),
            PostEntity(
                authorName = "Club Velvet VIP",
                authorType = "Club Oficial • Verificado",
                authorAges = "Exclusivo",
                location = "Barcelona • Eixample",
                avatarColor = 0xFF8B5CF6,
                avatarInitials = "VV",
                content = "🎭 Esta noche: Masquerade Night & Champagne Lounge. Un espacio seguro, elegante y discreto para parejas y personas selectas. Zona de baile, lounge privado y dress code elegante obligatorio con antifaz. Aforo limitado para garantizar comodidad.",
                lifestyleTag = "Fiesta / Evento",
                imageResName = "img_event_banner",
                likesCount = 84,
                commentsCount = 19,
                userReaction = "FIRE",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 180
            ),
            PostEntity(
                authorName = "Laura & Marcos",
                authorType = "Pareja Primeriza",
                authorAges = "29 & 32",
                location = "Valencia • Ruzafa",
                avatarColor = 0xFFF59E0B,
                avatarInitials = "LM",
                content = "Cumplimos nuestros primeros meses en este estilo de vida y queríamos agradecer a los que compartieron sus pautas de comunicación en el grupo. Entender que el consentimiento y el tiempo de cada uno es sagrado nos ha unido mucho más como pareja. ❤️✨",
                lifestyleTag = "Experiencia & Reflexión",
                imageResName = "",
                likesCount = 57,
                commentsCount = 12,
                userReaction = "LOVE",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 360
            ),
            PostEntity(
                authorName = "Elena Moon",
                authorType = "Single Chica",
                authorAges = "27",
                location = "Marbella / Costa del Sol",
                avatarColor = 0xFFEC4899,
                avatarInitials = "EM",
                content = "Tarde de relax en la piscina. Siempre me preguntan si las chicas solas nos sentimos cómodas en eventos swinger: con parejas respetuosas y educadas, el ambiente es inmejorable. Un brindis por la gente que sabe disfrutar con clase y respeto. 🥂",
                lifestyleTag = "Comunidad Lifestyle",
                imageResName = "img_lifestyle_banner",
                likesCount = 46,
                commentsCount = 8,
                userReaction = "CHEERS",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 720
            ),
            PostEntity(
                authorName = "David & Marta",
                authorType = "Pareja Experimentada",
                authorAges = "39 & 37",
                location = "Sevilla • Triana",
                avatarColor = 0xFF10B981,
                avatarInitials = "DM",
                content = "¿Cuál es vuestra regla de oro como pareja antes de acudir a una fiesta o quedar con otra pareja? Para nosotros: siempre decidir juntos en el momento, derecho a veto sin explicaciones y volver juntos a casa.",
                lifestyleTag = "Debate & Consejos",
                imageResName = "",
                likesCount = 73,
                commentsCount = 24,
                userReaction = "",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 1440
            )
        )
        socialDao.insertPosts(initialPosts)

        // Seed Comments
        val initialComments = listOf(
            CommentEntity(
                postId = 1,
                authorName = "David & Marta",
                authorType = "Pareja",
                avatarInitials = "DM",
                avatarColor = 0xFF10B981,
                text = "¡Excelente plan! Nosotros estamos en Madrid este fin de semana, os mandamos mensaje privado.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 30
            ),
            CommentEntity(
                postId = 1,
                authorName = "Elena Moon",
                authorType = "Single Chica",
                avatarInitials = "EM",
                avatarColor = 0xFFEC4899,
                text = "¡Que disfrutéis mucho la velada chicos!",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 15
            ),
            CommentEntity(
                postId = 2,
                authorName = "Alex & Sofía",
                authorType = "Pareja",
                avatarInitials = "AS",
                avatarColor = 0xFFE11D62,
                text = "Ya tenemos nuestros antifaces listos, nos vemos el sábado.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 120
            )
        )
        socialDao.insertComments(initialComments)

        // Seed Events
        val initialEvents = listOf(
            EventEntity(
                title = "Masquerade Midnight Gala 🎭",
                category = "Club VIP Exclusivo",
                dateStr = "Este Sábado, 14 Oct",
                timeStr = "23:00 - 05:00",
                city = "Barcelona",
                venue = "Club Velvet VIP (Dirección discreta por confirmación)",
                dressCode = "Gala Negra, Antifaz & Lencería Elegante",
                description = "Noche anual de misterio y seducción. Coctelería de autor, música deep house y áreas privadas con ambientación de lujo.",
                attendeesCount = 68,
                isAttending = true,
                imageResName = "img_event_banner"
            ),
            EventEntity(
                title = "Penthouse Sunset & Champagne 🥂",
                category = "Fiesta Privada en Villa",
                dateStr = "Viernes, 20 Oct",
                timeStr = "20:30 - 03:00",
                city = "Madrid Norte",
                venue = "Villa Privada con Piscina Climatizada",
                dressCode = "Cocktail Chic / Elegante informal",
                description = "Reunión reducida para 20 parejas seleccionadas. Barbacoa gourmet, cava de bienvenida y ambiente distendido para conocerse.",
                attendeesCount = 24,
                isAttending = false,
                imageResName = "img_lifestyle_banner"
            ),
            EventEntity(
                title = "Cena a Ciegas para Parejas Afines 🍷",
                category = "Cena Gourmet de Parejas",
                dateStr = "Jueves, 26 Oct",
                timeStr = "21:00 - 00:30",
                city = "Valencia",
                venue = "Salón Reservado Restaurante Lounge",
                dressCode = "Smart Casual & Elegante",
                description = "Cena de 4 tiempos en mesa compartida de parejas con mentalidad abierta. Sin presiones, enfoque 100% en simpatía y química.",
                attendeesCount = 14,
                isAttending = false,
                imageResName = "img_lifestyle_banner"
            ),
            EventEntity(
                title = "All-White Sensation Resort Getaway 🌴",
                category = "Weekend Takeover",
                dateStr = "10 - 12 Noviembre",
                timeStr = "Fin de semana completo",
                city = "Ibiza",
                venue = "Hotel Boutique 5 Estrellas Reservado",
                dressCode = "Total White & Bañadores de Diseño",
                description = "Fin de semana solo para adultos en hotel boutique reservado exclusivamente para la comunidad swinger internacional.",
                attendeesCount = 92,
                isAttending = true,
                imageResName = "img_event_banner"
            )
        )
        socialDao.insertEvents(initialEvents)

        // Seed Groups
        val initialGroups = listOf(
            GroupEntity(
                name = "Parejas Primerizas & Primeros Pasos",
                category = "Iniciación & Consejos",
                description = "Espacio dedicado a resolver dudas, compartir miedos y aprender a establecer límites seguros y sanos en el estilo de vida.",
                membersCount = 1420,
                isJoined = true,
                badgeEmoji = "🥂"
            ),
            GroupEntity(
                name = "Viajes, Cruceros y Hoteles Swinger",
                category = "Turismo Lifestyle",
                description = "Opiniones, reservas conjuntas, experiencias en resorts para adultos y escapadas de fin de semana.",
                membersCount = 2890,
                isJoined = false,
                badgeEmoji = "✈️"
            ),
            GroupEntity(
                name = "Eventos Privados & Cenas Madrid/BCN",
                category = "Quedadas Locales",
                description = "Coordinación de cenas gourmet, quedadas en coctelerías y fiestas privadas con aforo controlado.",
                membersCount = 3150,
                isJoined = true,
                badgeEmoji = "🎭"
            ),
            GroupEntity(
                name = "Ética, Respeto y Consentimiento",
                category = "Comunidad & Valores",
                description = "Reglas de convivencia, respeto irrestricto al 'No', educación y buenas prácticas para una comunidad impecable.",
                membersCount = 4210,
                isJoined = true,
                badgeEmoji = "🛡️"
            )
        )
        socialDao.insertGroups(initialGroups)

        // Seed Initial Messages
        val initialMessages = listOf(
            ChatMessageEntity(
                conversationId = "chat_carlos_valeria",
                senderName = "Carlos & Valeria",
                text = "¡Hola Alex y Sofía! Vimos vuestro perfil y nos encantó vuestra presentación. ¿Soléis ir por el centro a tomar algo?",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 120,
                isMine = false
            ),
            ChatMessageEntity(
                conversationId = "chat_carlos_valeria",
                senderName = "Alex & Sofía",
                text = "¡Hola chicos! Sí, casi siempre por el barrio de Salamanca o Chamberí. Nos encanta la buena coctelería.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 95,
                isMine = true
            ),
            ChatMessageEntity(
                conversationId = "chat_carlos_valeria",
                senderName = "Carlos & Valeria",
                text = "¡Genial! Este viernes podríamos coincidir en una terraza tranquila y charlar sin prisas a ver qué tal la sintonía.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 50,
                isMine = false
            ),
            ChatMessageEntity(
                conversationId = "chat_club_velvet",
                senderName = "Club Velvet",
                text = "Estimados Alex & Sofía, vuestra reserva para la Masquerade Gala ha sido confirmada con acceso al lounge VIP.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 300,
                isMine = false
            ),
            ChatMessageEntity(
                conversationId = "chat_elena_moon",
                senderName = "Elena Moon",
                text = "¡Hola pareja! Me pareció muy linda vuestra foto de perfil. ¡Saludos desde Valencia!",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 500,
                isMine = false
            )
        )
        socialDao.insertChatMessages(initialMessages)
    }
}
