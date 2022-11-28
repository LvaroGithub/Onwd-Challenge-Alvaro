package com.onwd.challenge.devicelist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onwd.challenge.R
import com.onwd.challenge.databinding.ItemDeviceBinding
import com.onwd.challenge.devicelist.model.DeviceView
import com.onwd.devices.DeviceType.TYPE_LAPTOP
import com.onwd.devices.DeviceType.TYPE_PHONE
import com.onwd.devices.DeviceType.TYPE_WATCH
import com.onwd.devices.IDevice

class DeviceListAdapter : ListAdapter<DeviceView, DeviceListAdapter.DeviceViewHolder>(FileViewComparator()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(device: DeviceView) {
            binding.apply {
                tvName.text = device.name
                ivIcon.setImageDrawable(ContextCompat.getDrawable(ivIcon.context, device.iconRes))
            }
        }
    }

    class FileViewComparator : DiffUtil.ItemCallback<DeviceView>() {
        override fun areItemsTheSame(oldItem: DeviceView, newItem: DeviceView): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DeviceView, newItem: DeviceView): Boolean {
            return oldItem.name == newItem.name
        }
    }
}