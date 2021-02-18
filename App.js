import React, {Component} from 'react';
import {View, Text} from "react-native";

import firebase from 'firebase/app';
import 'firebase/firestore';
import {Provider} from 'react-redux';
import {createStore, applyMiddleware} from "redux";
import rootReducer from './redux/reducers'
import thunk from 'redux-thunk'

const firebaseConfig = {
    apiKey: "AIzaSyB5aMZCl21pEz1UbE83AuxdWVPSFOsRATk",
    authDomain: "bokjipang.firebaseapp.com",
    projectId: "bokjipang",
    storageBucket: "bokjipang.appspot.com",
    messagingSenderId: "626259617240",
    appId: "1:626259617240:web:0386780264c19156b6fead",
    measurementId: "G-N6THEHK4M5"
};

if(firebase.apps.length === 0) {
    firebase.initializeApp(firebaseConfig)
}

import {NavigationContainer} from "@react-navigation/native";
import {createStackNavigator} from '@react-navigation/stack';

import LoginScreen from "./Components/auth/Login";
import MainScreen from "./Components/Main";

const Stack = createStackNavigator();

export class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loaded: false
        }
    }

    componentDidMount() {
        setTimeout(() => {
            this.setState({
                loaded: true
            });
        }, 2000);
    }

    render() {
        const {loaded} = this.state;
        if (!loaded) {
            return (
                <View style={{flex: 1, justifyContent: 'center'}}>
                    <Text>Loading</Text>
                </View>
            )
        }
        return (
            <NavigationContainer>
                <Stack.Navigator initialRouteName="Landing">
                    <Stack.Screen name="Login" component={ MainScreen } options={{headerShown: false}}/>
                </Stack.Navigator>
            </NavigationContainer>
        )
    }
}

export default App
