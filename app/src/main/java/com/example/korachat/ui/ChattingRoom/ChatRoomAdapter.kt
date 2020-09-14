package com.example.korachat.ui.ChattingRoom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.korachat.R
import com.example.korachat.models.ChatMessages
import kotlinx.android.synthetic.main.message_received.view.*
import kotlinx.android.synthetic.main.message_received.view.date
import kotlinx.android.synthetic.main.message_received.view.message
import kotlinx.android.synthetic.main.message_sent.view.*

private const val MESSAGE_TYPE_RECEIVED = 0
private const val MESSAGE_TYPE_SENT = 1

class ChatRoomAdapter(
    var context: Context,
    var chatMessages: List<ChatMessages>,
    var currentUser: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].sentBy == currentUser) {
            MESSAGE_TYPE_SENT
        } else {
            MESSAGE_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MESSAGE_TYPE_SENT) {
            return SentMessages(
                LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
            )
        } else {
            return ReceivedMessages(
                LayoutInflater.from(context).inflate(R.layout.message_received, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MESSAGE_TYPE_SENT) {
            (holder as SentMessages).bind(chatMessages[position])
        } else {
            (holder as ReceivedMessages).bind(chatMessages[position])

        }
    }

    class ReceivedMessages(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(chatMessage: ChatMessages) {
            if (chatMessage.message.isNullOrEmpty()) {
                itemView.ln_received_image.visibility=View.VISIBLE
                itemView.ln_received_message.visibility=View.GONE
                itemView.received_image_date.text=chatMessage.messageDate
                Glide.with(itemView.context)
                    .load(chatMessage.imageUrl)
                    .fitCenter()
                    .placeholder(R.drawable.ic_image_white_24dp)
                    .into(itemView.receive_image)
            }
            if (chatMessage.imageUrl.isNullOrEmpty()) {
                itemView.message.text = chatMessage.message
                itemView.date.text = chatMessage.messageDate
                itemView.ln_received_image.visibility=View.GONE
                itemView.ln_received_message.visibility=View.VISIBLE

            }
        }
    }

    class SentMessages(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(chatMessage: ChatMessages) {
            if (chatMessage.message.isNullOrEmpty()) {
                itemView.ln_sent_message.visibility=View.GONE
                itemView.ln_sent_image.visibility=View.VISIBLE
                itemView.sent_image_date.text=chatMessage.messageDate
                Glide.with(itemView.context)
                    .load(chatMessage.imageUrl)
                    .fitCenter()
                    .placeholder(R.drawable.ic_image_white_24dp)
                    .into(itemView.send_image)
            }
            if (chatMessage.imageUrl.isNullOrEmpty()) {
                itemView.message.text = chatMessage.message
                itemView.date.text = chatMessage.messageDate
                itemView.ln_sent_image.visibility=View.GONE
                itemView.ln_sent_message.visibility=View.VISIBLE
            }

        }
    }
}