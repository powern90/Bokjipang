//메인페이지
import React, {Component} from 'react';
import {SliderBox} from "react-native-image-slider-box";
import {StyleSheet, View, Text} from "react-native";

export class Banner extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentIndex: 1,
            images: [
                "https://source.unsplash.com/1024x768/?nature",
                "https://source.unsplash.com/1024x768/?water",
                "https://source.unsplash.com/1024x768/?girl",
                "https://source.unsplash.com/1024x768/?tree"
            ]
        };
    }

    render() {
        return (
            <View style={styles.container}>
                <SliderBox
                    autoplay={true}
                    circleLoop={true}
                    images={this.state.images}
                    dotColor="rgba(0,0,0,0)"
                    inactiveDotColor="rgba(0,0,0,0)"
                    currentImageEmitter={(index) => {
                        this.setState({
                            currentIndex: index + 1,
                        });
                    }}
                />
                <View
                    style={styles.image_number_container}>
                    <Text style={styles.image_number}>
                        {this.state.currentIndex}/{this.state.images.length}
                    </Text>
                </View>
            </View>
        )
    }
}


const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
        height: '100%',
        flexDirection: 'row',
        overflow: 'hidden'
    },
    image_number_container: {
        position: 'absolute',
        bottom: '8%',
        right: 0,
        paddingTop: 4,
        paddingRight: 6,
        paddingBottom: 4,
        paddingLeft: 10,
        borderTopLeftRadius: 14,
        borderBottomLeftRadius: 14,
        backgroundColor: 'rgba(0,0,0,0.6)'
    },
    image_number: {
        fontSize: 10,
        color: '#ffffff'
    }
});

export default Banner
