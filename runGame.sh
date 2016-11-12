#!/bin/bash

# compile bots
sbt compile

# clean game directory
if [ -d "games" ]; then 
  rm -r games
fi
mkdir games

# copy bots to game directory
cp target/scala-2.10/classes/* games/
cp halite.exe games/

# run game
pushd games
./halite -d "30 30" "scala RLBot" "scala MyBot"
popd