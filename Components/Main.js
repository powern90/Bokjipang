//네비게이션바
import React, { Component } from 'react';
import { Text, View } from "react-native";

import { createMaterialBottomTabNavigator} from "@react-navigation/material-bottom-tabs";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";

import HomeScreen from './main/Home';
import BoardScreen from './main/board/Board';
import SupportScreen from './main/support/Support';

const Tab = createMaterialBottomTabNavigator();

export class Main extends Component {
    render() {
        return (
            <Tab.Navigator initialRouteName='Home' labeled={false}>
                <Tab.Screen name="Home" component={ HomeScreen }
                            options={{
                                tabBarIcon: ({ color, size }) => (
                                    <MaterialCommunityIcons name='home' color={color} size={26}/>
                                )
                            }}
                />
                <Tab.Screen name="Board" component={ BoardScreen }
                            options={{
                                tabBarIcon: ({ color, size }) => (
                                    <MaterialCommunityIcons name='account-circle' color={color} size={26}/>
                                )
                            }}
                />
                <Tab.Screen name="Support" component={ SupportScreen }
                            options={{
                                tabBarIcon: ({ color, size }) => (
                                    <MaterialCommunityIcons name='account-circle' color={color} size={26}/>
                                )
                            }}
                />
            </Tab.Navigator>
        );
    }
}

export default Main
