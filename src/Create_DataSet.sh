#!/bin/bash

# Check if the peer index is provided
if [ $# -ne 1 ]; then
    echo "Usage: $0 PeerIndex"
    exit 1
fi

peer_idx=$1
folder="./shared"
count_small_files=0
count_large_files=0

# Check if the directory exists. If not, create it.
if [ ! -d "$folder" ]; then
    mkdir "$folder"
fi

echo "Generating 100K small (10KB) files..."

# Create 100K: 10KB text files
for serial in {1..100000}; do
    dd if=/dev/zero of="$folder/10KB_${peer_idx}_$serial.txt" bs=10K count=1 > /dev/null 2>&1
    ((count_small_files++))

    # Provide periodic status updates
    if [ $count_small_files -eq 1000 ]; then
        echo "Processed $count_small_files small files..."
        count_small_files=0
    fi
done

echo "Generating 10 large (100MB) files..."

# Create 10: 100MB binary files
for serial in {1..10}; do
    dd if=/dev/zero of="$folder/100MB_${peer_idx}_$serial.bin" bs=100M count=1 > /dev/null 2>&1
    ((count_large_files++))

    # Provide periodic status updates
    if [ $count_large_files -eq 1 ]; then
        echo "Processed $count_large_files large file..."
        count_large_files=0
    fi
done

echo "Dataset generation completed."
